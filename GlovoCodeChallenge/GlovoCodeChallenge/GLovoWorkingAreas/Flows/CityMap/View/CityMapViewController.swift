//
//  CityMapViewController.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/27/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import UIKit
import RxCocoa
import RxSwift
import CoreLocation
import SnapKit
import RxDataSources
import GoogleMaps

class CityMapViewController: UIViewController {
    
    @IBOutlet weak var cityInfoTableView: UITableView! {
        didSet {
            cityInfoTableView.register(CityInfoCell.self, forCellReuseIdentifier: CityInfoCell.identifier)
            cityInfoTableView.separatorStyle = .none
        }
    }
    
    @IBOutlet weak var mapOuterView: UIView!
    private var currentLocation: CLLocationCoordinate2D?
    private var mapView: GMSMapView!
    private var disposeBag = DisposeBag()
    private var city: City?
    private let viewModel: CityMapViewModel
    private var cities: [City] = []
    private var mapZoom: Float = 13
    private var currentMapZoom: Float = 0
    private var countries: [Country] = []
    private var citiesCoordinates: [String: CLLocationCoordinate2D] = [:]
    private var citiesWithPathBounds:[String: GMSCoordinateBounds] = [:]
    
    init(_ viewModel: CityMapViewModel, currentLocation: CLLocationCoordinate2D?) {
        self.viewModel = viewModel
        self.currentLocation = currentLocation
        super.init(nibName: "CityMapViewController", bundle: nil)
    }
    
    required init?(coder aDecoder: NSCoder) {
        return nil
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mapView.delegate = self
        setUI()
        bind()
    }
    
    deinit {
        mapView.clear()
        disposeBag = DisposeBag()
    }
}

extension CityMapViewController {
    
    private func setUI() {
        let cameraPosition = GMSCameraPosition.camera(withLatitude: 0, longitude: 0, zoom: mapZoom)
        mapView = GMSMapView(frame: mapOuterView.frame, camera: cameraPosition)
        self.mapOuterView.addSubview(mapView)
        
        cityInfoTableView.snp.makeConstraints { make -> Void in
            make.top.equalToSuperview().inset(0)
            make.leading.trailing.equalToSuperview()
            make.bottom.equalTo(mapOuterView).inset(0)
        }
        
        mapOuterView.snp.makeConstraints { make -> Void in
            make.top.equalTo(cityInfoTableView).inset(0)
            make.leading.trailing.equalToSuperview()
            make.bottom.equalToSuperview()
        }
        
        mapView.snp.makeConstraints { make -> Void in
            make.bottom.leading.trailing.top.equalToSuperview()
        }
    }
    
    private func bind() {
        viewModel.countries?.asObservable()
            .subscribe(onNext: { [weak self ] countries in
                 guard let `self` = self else { return }
                 self.countries = countries
                 self.setCities(for: countries)
            }).disposed(by: disposeBag)
    }
    
    func createDataSource() -> RxTableViewSectionedReloadDataSource<CityMapSection> {
        let dataSource = RxTableViewSectionedReloadDataSource<CityMapSection>(configureCell: { (dataSource: TableViewSectionedDataSource<CityMapSection>, tableView: UITableView, indexPath: IndexPath, item: City) in
            guard let cell: CityInfoCell = self.cityInfoTableView.dequeueReusableCell(withIdentifier: CityInfoCell.identifier) as? CityInfoCell else {
                fatalError("Couldnt find reference for CountryListCell")
            }
            cell.buildCell(for: item)
            return cell
        })
        return dataSource
    }
}

extension CityMapViewController {
    
    func centerMapOnCity(on coordinates: CLLocationCoordinate2D? = nil) {
        guard let currentCity = viewModel.city else { return }
        let coordinates = viewModel.getCoordinatesForCity(currentCity)
        let camera = GMSCameraUpdate.setTarget(coordinates, zoom: mapZoom)
        mapView.animate(with: camera)
    }
    
    private func setCities(for countries: [Country]) {
        countries.forEach{ [weak self] country in
            guard let `self` = self else { return }
            self.displayCityMarkers(for: country.cities)
            self.setWorkingAreas(for: country.cities)
        }
    }
    
    private func displayItemsInMap(for cities: [City]) {
        self.cities = cities
        setArrayBounds(for: cities)
        if let location = self.currentLocation,
            self.viewModel.isLocationInMapBounds(location, bounds: citiesWithPathBounds) {
            let camera = GMSCameraPosition.camera(withTarget: location, zoom: 12)
            mapView.animate(to: camera)
        } else if let city = self.viewModel.city,
             !city.code.isEmpty {
             setTableView()
        } else {
            return
        }
    }
    
    private func displayCityMarkers(for cities: [City]) {
        mapView.clear()
        cities.forEach { [weak self] city in
            guard let `self` = self else { return }
            let marker = self.viewModel.getCityMarker(city)
            self.citiesCoordinates[city.code] = marker.position
            marker.map = self.mapView
        }
    }
    
    private func setWorkingAreas(for cities: [City]) {
        mapView.clear()
        cities.forEach { [weak self] city in
            guard let `self` = self else { return }
            self.displayWorkingAreas(for: city)
        }
    }
    
    private func displayWorkingAreas(for city: City) {
        let workingAreaPaths = viewModel.getWorkingAreas(city.workingArea)
        workingAreaPaths.forEach{ [weak self] path in
            guard let `self` = self else { return }
            let polygon = GMSPolygon(path: path)
            polygon.map = self.mapView
        }
    }
    
    private func setArrayBounds(for cities: [City]) {
        cities.forEach{ [weak self] city in
            guard let `self` = self else { return }
            let paths = self.viewModel.getWorkingAreas(city.workingArea)
            let cityBounds = self.viewModel.getCityBounds(for: paths)
            citiesWithPathBounds[city.code] = cityBounds
        }
        
    }
    
    private func setTableView() {
        let dataSource =  createDataSource()
        
        viewModel.cityInfo?
            .map(viewModel.setCityInfo)
            .drive(cityInfoTableView.rx.items(dataSource: dataSource))
            .disposed(by: disposeBag)
        
        dataSource.titleForHeaderInSection = {
            dataSource, index in
            return dataSource.sectionModels[index].header
        }
        
        cityInfoTableView.rx
            .setDelegate(self)
            .disposed(by: disposeBag)
    }
}

extension CityMapViewController: UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        return CGSize(width: collectionView.contentSize.width, height: 300)
        
    }
    
    func collectionView(_ collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                        insetForSectionAt section: Int) -> UIEdgeInsets {
        
        return UIEdgeInsets(top:0, left: 0, bottom: 0, right: 0)
    }
}

extension CityMapViewController: GMSMapViewDelegate {
    func mapView(_ mapView: GMSMapView, didChange position: GMSCameraPosition) {
        currentMapZoom = position.zoom
    }
    
    func mapView(_ mapView: GMSMapView, didTap marker: GMSMarker) -> Bool {
        //centerMap(on: marker)
        return true
    }
}

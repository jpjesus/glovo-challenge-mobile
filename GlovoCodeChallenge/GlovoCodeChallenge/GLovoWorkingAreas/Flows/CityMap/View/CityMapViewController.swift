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
import SwiftSpinner

class CityMapViewController: UIViewController {
    
    @IBOutlet weak var cityInfoTableView: UITableView! {
        didSet {
            cityInfoTableView.register(CityInfoCell.self, forCellReuseIdentifier: CityInfoCell.identifier)
            cityInfoTableView.separatorStyle = .none
            cityInfoTableView.bounces = false
        }
    }
    
    @IBOutlet weak var mapOuterView: UIView!
    private var currentLocation: CLLocationCoordinate2D?
    private var mapView: GMSMapView!
    private var disposeBag = DisposeBag()
    private var city: City?
    private let viewModel: CityMapViewModel
    private var cities: [City] = []
    private var mapZoom: Float = 8
    private var currentMapZoom: Float = 0
    private var countries: [Country] = []
    private var citiesCoordinates: [String: CLLocationCoordinate2D] = [:]
    private var citiesWithPathBounds: [String: GMSCoordinateBounds] = [:]
    
    init(_ viewModel: CityMapViewModel, currentLocation: CLLocationCoordinate2D?) {
        self.viewModel = viewModel
        self.currentLocation = currentLocation
        super.init(nibName: "CityMapViewController", bundle: nil)
    }
    
    required init?(coder aDecoder: NSCoder) {
        return nil
    }
    
    override func viewWillAppear(_ animated: Bool) {
         navigationController?.setNavigationBarHidden(false, animated: true)
        navigationItem.title = "Map information"
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUI()
        bind()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        mapView.clear()
        mapView.delegate = nil
        mapView.removeFromSuperview()
        mapView = nil
        cityInfoTableView.dataSource = nil
    }
    
    deinit {
        disposeBag = DisposeBag()
    }
}

extension CityMapViewController {
    
    private func setUI() {
        let cameraPosition = GMSCameraPosition.camera(withLatitude: 0, longitude: 0, zoom: mapZoom)
        mapView = GMSMapView(frame:.zero, camera: cameraPosition)
        self.mapOuterView.addSubview(mapView)
        
        cityInfoTableView.snp.makeConstraints { make -> Void in
            make.top.equalToSuperview()
            make.leading.trailing.equalToSuperview()
            make.height.equalTo(250)
            make.bottom.equalTo(mapOuterView.snp.top)
        }
        
        mapOuterView.snp.makeConstraints { make -> Void in
            make.top.equalTo(cityInfoTableView.snp.bottom).inset(0)
            make.leading.trailing.equalToSuperview()
            make.bottom.equalToSuperview()
        }
        
        mapView.snp.makeConstraints { make -> Void in
            make.bottom.leading.trailing.top.equalTo(mapOuterView)
        }
        mapView.delegate = self
    }
    
    private func bind() {
        
        viewModel.countries?.asObservable()
            .subscribe(onNext: { [weak self ] countries in
                SwiftSpinner.show(duration: 0.5, title: "Searching for working areas ...")
                guard let `self` = self else { return }
                self.setCities(for: countries)
            }).disposed(by: disposeBag)
        
        viewModel.cityInfo?.asObservable()
            .subscribe(onNext: { [weak self ] city in
                SwiftSpinner.show(duration: 0.5, title: "Searching for working areas ...")
                guard let `self` = self else { return }
                self.cities.append(city)
                self.displayWorkingAreas(for: city)
                self.setBoundForCities(for: [city])
                self.displayLocationInMap()
            }).disposed(by: disposeBag)
        
        setTableView()
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
    
    func centerMapOnCity(on marker: GMSMarker) {
        guard let code = citiesCoordinates.first(where: { $0.value == marker.position })?.key else { return }
        guard let bounds = citiesWithPathBounds[code] else { return }
        let camera = GMSCameraUpdate.fit(bounds)
        mapView.animate(with: camera)
    }
    
    private func setCities(for countries: [Country]) {
        countries.forEach{ [weak self] country in
            guard let `self` = self else { return }
            self.countries.append(country)
            self.setWorkingAreas(for: country.cities)
            self.displayCityMarkers(for: country.cities)
            self.setBoundForCities(for: country.cities)
        }
        displayLocationInMap()
    }
    
    private func setBoundForCities(for cities: [City]) {
        setArrayBounds(for: cities)
    }
    
    private func displayLocationInMap() {
        if let location = self.currentLocation,
            self.viewModel.isLocationInMapBounds(location, bounds: citiesWithPathBounds) {
            let camera = GMSCameraPosition.camera(withTarget: location, zoom: 12)
            let code = self.viewModel.retreiveCityCode(location, bounds: citiesWithPathBounds)
            let city = cities.first(where:{$0.code ==  code})
            viewModel.cityInfo = Driver<City>.just(city ?? City())
            viewModel.cityInfo?
                     .map(viewModel.setCityInfo)
                     .drive(cityInfoTableView.rx.items(dataSource: createDataSource()))
                     .disposed(by: disposeBag)
            mapView.animate(to: camera)
        } else if let city = self.viewModel.city,
            !city.code.isEmpty {
            SwiftSpinner.hide {
                self.centerOnCity(with: city.code)
            }
        } else {
            SwiftSpinner.hide {
                self.showAlertError(with: self.showAlert(with: "Not working areas available for given location. Please select another location from our country list."))
            }
        }
    }
    
    private func displayCityMarkers(for cities: [City]) {
        mapView.clear()
        cities.forEach { [weak self] city in
            guard let `self` = self else { return }
            self.cities.append(city)
            let marker = self.viewModel.getCityMarker(city)
            self.citiesCoordinates[city.code] = marker.position
            marker.map = self.mapView
        }
    }
    
    private func setWorkingAreas(for cities: [City]) {
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
    
    private func displaAllyWorkingAreas() {
        mapView.clear()
        cities.forEach { [weak self] city in
            self?.displayWorkingAreas(for: city)
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
    
    private func displayWorkingAreasOrCities() {
        if currentMapZoom > mapZoom {
            displaAllyWorkingAreas()
        } else {
            displayCityMarkers(for: cities)
        }
    }
    
    private func centerOnCity(with code: String) {
        guard let city = cities.first(where: { $0.code == code }) else { return }
        let position = viewModel.getCoordinatesForCity(city)
        let camera = GMSCameraUpdate.setTarget(position, zoom: 12)
        mapView.animate(with: camera)
    }
    
    private func showAlertError(with alertView: UIAlertController) {
        let action = UIAlertAction(title: "Show country list", style: UIAlertAction.Style.default) { action in
            self.viewModel.showCountryList()
        }
        alertView.addAction(action)
        if let navigation = self.navigationController?.visibleViewController {
            if !(navigation.isKind(of: UIAlertController.self)) {
                OperationQueue.main.addOperation {
                    self.navigationController?.present(alertView, animated: true, completion: nil)
                }
            }
        }
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
        displayWorkingAreasOrCities()
    }
    
    func mapView(_ mapView: GMSMapView, didTap marker: GMSMarker) -> Bool {
        centerMapOnCity(on: marker)
        return true
    }
}

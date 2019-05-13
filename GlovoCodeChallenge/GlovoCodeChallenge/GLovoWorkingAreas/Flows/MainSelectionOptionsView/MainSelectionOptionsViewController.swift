//
//  MainSelectionOptionsViewController.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 5/12/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import UIKit
import RxSwift
import RxCocoa
import RxCoreLocation
import CoreLocation

class MainSelectionOptionsViewController: UIViewController {
    
    @IBOutlet weak var locationButton: UIButton! {
        didSet {
            locationButton.setTitle("Show working areas by location", for: .normal)
        }
    }
    @IBOutlet weak var cityListButton: UIButton! {
        didSet {
            cityListButton.setTitle("Find working areas by country", for: .normal)
        }
    }
    private var disposeBag = DisposeBag()
    private let viewModel: MainSelectionOptionsViewModel
    private var location: CLLocationCoordinate2D?
    private var authorizationStatus = CLLocationManager.authorizationStatus()
    
    init(_ viewModel: MainSelectionOptionsViewModel) {
        self.viewModel = viewModel
        super.init(nibName: "MainSelectionOptionsViewController", bundle: nil)
    }
    
    required init?(coder aDecoder: NSCoder) {
        return nil
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUI()
        bind()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: false)
    }
    
    private func bind() {
        
        locationButton.rx.tap
            .asObservable()
            .throttle(0.5, latest: false, scheduler: MainScheduler.instance)
            .subscribe(onNext:{ [weak self] _ in
                guard let `self` = self else { return }
                self.showMapWithCurrentLocation()
            }).disposed(by: disposeBag)
        
        cityListButton.rx.tap
            .asObservable()
            .throttle(0.5, latest: false, scheduler: MainScheduler.instance)
            .subscribe(onNext:{ [weak self] _ in
                guard let `self` = self else { return }
                self.viewModel.showCityList()
            }).disposed(by: disposeBag)
    }
    
    private func showMapWithCurrentLocation() {
        
        if  CLLocationManager.authorizationStatus() == .denied {
            self.showAlertGoToSettings()
        }
        
        self.viewModel.locationManager.getLocation()
            .asObservable()
            .subscribe(onNext: { [weak self] coordinates in
                guard let `self` = self else { return }
                self.viewModel.showMap(with: coordinates)
            }).disposed(by: disposeBag)
    }
    
    private func setUI() {
        locationButton.snp.makeConstraints { make in
            make.center.equalToSuperview().offset(0)
            
        }
        
        cityListButton.snp.makeConstraints { make in
            make.top.equalTo(locationButton).inset(40)
            make.centerX.equalTo(locationButton)
        }
    }
}

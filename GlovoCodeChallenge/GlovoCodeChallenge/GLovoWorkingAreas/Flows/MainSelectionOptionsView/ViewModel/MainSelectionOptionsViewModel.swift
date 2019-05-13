//
//  MainSelectionOptionsViewModel.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 5/12/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import Foundation
import CoreLocation
import RxCoreLocation

class MainSelectionOptionsViewModel {
    
    private weak var coordinator: MainSelectionOptionsCoordinator?
    var locationManager = LocationManager.sharedInstance
    
    init(_ coordinator: MainSelectionOptionsCoordinator) {
        self.coordinator = coordinator
    }
    
    func showMap(with coordinates: CLLocationCoordinate2D?) {
        coordinator?.gotoMapView(with: coordinates ?? CLLocationCoordinate2D())
    }
    
    func showCityList() {
      coordinator?.gotoCountryListView()
    }
}

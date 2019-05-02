//
//  CityMapCoordinator.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/27/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import UIKit
import CoreLocation

class CityMapCoordinator: Coordinator {
    
    var childCoordinators: [Coordinator] = []
    
    private weak var mainCoordinator: Coordinator?
    private weak var navigation: UINavigationController?
    private var city: City?
    private let coordinates: CLLocationCoordinate2D?
    
    init(_ navigation: UINavigationController?, mainCoordinator: Coordinator, city: City?, coordinates: CLLocationCoordinate2D? = nil) {
        self.navigation = navigation
        self.mainCoordinator = mainCoordinator
        self.city = city
        self.coordinates = coordinates
    }
    
    func start() {
        let viewModel = CityMapViewModel(self, city: self.city, currentLocation: coordinates)
        let vc = CityMapViewController(viewModel, currentLocation: coordinates)
        navigation?.pushViewController(vc, animated: true)
    }
    
    func finish() {}
}

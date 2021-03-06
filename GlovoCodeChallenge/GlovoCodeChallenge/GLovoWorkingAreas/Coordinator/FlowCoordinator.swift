//
//  FlowCoordinator.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/20/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import UIKit
import CoreLocation

class FlowCoordinator: Coordinator {
    
    private let window: UIWindow?
    private let parent = UINavigationController()
    
    var childCoordinators: [Coordinator] = []
    
    init(window: UIWindow?) {
        self.window = window
        window?.rootViewController = parent
        window?.makeKeyAndVisible()
    }
    
    func start() {
        showMainView()
    }
    
    private func showMainView() {
        let coordinator = MainSelectionOptionsCoordinator(parent, mainCoordinator: self)
        addCoordinatorChild(coordinator)
        coordinator.start()
    }
    
    func showCountryListView() {
        let coordinator = CountryListCoordinator(parent, mainCoordinator: self)
        addCoordinatorChild(coordinator)
        coordinator.start()
    }
    
    func showMapView(with coordinates: CLLocationCoordinate2D?) {
        let coordinator = CityMapCoordinator(parent, mainCoordinator: self, coordinates: coordinates)
        addCoordinatorChild(coordinator)
        coordinator.start()
    }
    
    func showMapView(with city: City? = nil) {
        
        let coordinator = CityMapCoordinator(parent, mainCoordinator: self, city: city)
        addCoordinatorChild(coordinator)
        coordinator.start()
        
    }
}

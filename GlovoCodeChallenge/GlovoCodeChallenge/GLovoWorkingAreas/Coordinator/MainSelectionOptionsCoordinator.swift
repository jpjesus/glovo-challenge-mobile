//
//  MainSelectionOptionsCoordinator.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 5/12/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import UIKit
import CoreLocation

class MainSelectionOptionsCoordinator: Coordinator {
    
    var childCoordinators: [Coordinator] = []
    
    private weak var mainCoordinator: FlowCoordinator?
    private weak var navigation: UINavigationController?
    private var coordinates: CLLocationCoordinate2D?
    
    init(_ navigation: UINavigationController?, mainCoordinator: FlowCoordinator) {
        self.navigation = navigation
        self.mainCoordinator = mainCoordinator
    }
    
    func start() {
        let viewModel = MainSelectionOptionsViewModel(self)
        let vc = MainSelectionOptionsViewController(viewModel)
        navigation?.pushViewController(vc, animated: true)
    }
    
    func finish() {}
    
    func gotoMapView(with coordinates: CLLocationCoordinate2D) {
        mainCoordinator?.showMapView(with: coordinates)
    }
    
    func gotoCountryListView() {
        mainCoordinator?.showCountryListView()
    }
}

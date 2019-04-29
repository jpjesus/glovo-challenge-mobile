//
//  CountryListCoordinator.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/21/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import UIKit

class CountryListCoordinator: Coordinator {
    
    var childCoordinators: [Coordinator] = []
    
    private weak var mainCoordinator: FlowCoordinator?
    private weak var navigation: UINavigationController?
    
    init(_ navigation: UINavigationController?, mainCoordinator: FlowCoordinator) {
        self.navigation = navigation
        self.mainCoordinator = mainCoordinator
    }
    
    func start() {
        let viewModel = CountryListViewModel(self)
        let vc = CountryListViewController(viewModel)
        navigation?.pushViewController(vc, animated: true)
    }

    func gotoMapView(with city: City) {
        mainCoordinator?.showMapView(with: city, from: self)
    }
}

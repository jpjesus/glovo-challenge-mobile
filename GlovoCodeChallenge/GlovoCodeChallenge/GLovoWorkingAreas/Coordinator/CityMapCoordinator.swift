//
//  CityMapCoordinator.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/27/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import UIKit

class CityMapCoordinator: Coordinator {
    
    var childCoordinators: [Coordinator] = []
    
    private weak var mainCoordinator: Coordinator?
    private weak var navigation: UINavigationController?
    private var city: City?
    
    init(_ navigation: UINavigationController?, mainCoordinator: Coordinator, city: City?) {
        self.navigation = navigation
        self.mainCoordinator = mainCoordinator
        self.city = city
    }
    
    func start() {
//        let viewModel = CountryListViewModel(self)
//        let vc = CountryListViewController(viewModel)
//        navigation?.pushViewController(vc, animated: true)
    }
    
    func finish() {}
}

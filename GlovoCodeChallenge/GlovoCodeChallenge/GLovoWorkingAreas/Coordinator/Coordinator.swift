//
//  Coordinator.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/20/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import Foundation

protocol Coordinator: class {
    var childCoordinators: [Coordinator] { get set }
    func start()
    func finish()
}

extension Coordinator {
    func addCoordinatorChild(_ coordinator: Coordinator) {
        childCoordinators.append(coordinator)
    }
    
    func removeChildCoordinator(_ coordinator: Coordinator) {
        childCoordinators = childCoordinators.filter { $0 != coordinator }
    }
    
    func removeAllChildCoordinatorsWith<T>(type: T.Type) {
        childCoordinators = childCoordinators.filter { $0 is T  == false }
    }
    
    func removeAllChildCoordinators() {
        childCoordinators.removeAll()
    }
}

func == (lhs: Coordinator, rhs: Coordinator) -> Bool {
    return lhs === rhs
}

func != (lhs: Coordinator, rhs: Coordinator) -> Bool {
    return !(lhs == rhs)
}

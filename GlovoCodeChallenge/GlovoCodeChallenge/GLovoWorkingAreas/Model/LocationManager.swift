//
//  LocationManager.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 5/12/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import Foundation
import RxSwift
import RxCocoa
import RxCoreLocation
import CoreLocation

class LocationManager {
    
    private var manager = CLLocationManager()
    static var sharedInstance = LocationManager()
    private var disposeBag = DisposeBag()
    
    init() {
        manager.requestWhenInUseAuthorization()
        manager.startUpdatingLocation()
    }
    
    func getLocation() -> Single<CLLocationCoordinate2D> {
        return Single.create{ [weak self ] single in
            guard let `self` = self else { return Disposables.create() }
            self.manager.rx.location
                .subscribe(onNext: { location in
                    guard let location = location else { return }
                    single(.success( location.coordinate))
                }).disposed(by: self.disposeBag)
            return Disposables.create()
        }
    }
}

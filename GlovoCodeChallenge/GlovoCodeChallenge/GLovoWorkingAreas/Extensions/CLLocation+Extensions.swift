//
//  CLLocation+Extensions.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 5/12/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import CoreLocation

extension CLLocationCoordinate2D: Equatable {}

public func ==(lhs: CLLocationCoordinate2D, rhs: CLLocationCoordinate2D) -> Bool {
    return lhs.latitude == rhs.latitude && lhs.longitude == rhs.longitude
}


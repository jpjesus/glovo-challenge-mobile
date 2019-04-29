//
//  CityMapSection.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/27/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import Foundation
import RxSwift
import RxCocoa
import RxDataSources

struct CityMapSection {
    var header: String
    var items: [Item]
}

extension CityMapSection: SectionModelType {
    typealias Item = City
    
    init(original: CityMapSection, items: [Item]) {
        self = original
        self.items = items
    }
}

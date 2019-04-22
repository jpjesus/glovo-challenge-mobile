//
//  CountrySection.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/19/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import Foundation
import RxSwift
import RxCocoa
import RxDataSources

struct CountrySection {
    var header: String
    var items: [Item]
}

extension CountrySection: SectionModelType {
    typealias Item = City
    
    init(original: CountrySection, items: [Item]) {
        self = original
        self.items = items
    }
}

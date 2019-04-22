//
//  Country.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/19/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import Foundation

struct Country: Decodable {
    
    var name: String = ""
    var code: String = ""
    var cities: [City] = []
    
    enum CodingKeys: String, CodingKey {
        case name
        case code
    }
    
    init(name: String, code: String, cities: [City]) {
        self.name = name
        self.code = code
        self.cities = cities
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        code = try container.decode(String.self, forKey: .code)
        name = try container.decode(String.self, forKey: .name)

    }
}

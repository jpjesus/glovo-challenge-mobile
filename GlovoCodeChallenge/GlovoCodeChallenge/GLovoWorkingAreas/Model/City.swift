//
//  City.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/19/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import Foundation

struct City: Decodable {
    
    var code: String = ""
    var name: String = ""
    var countryCode: String = ""
    var currency: String = ""
    var isEnable: Bool =  false
    var isBusy: Bool = false
    var timeZone: String = ""
    var languageCode: String = ""
    var workingArea: [String] = []
    
    enum CodingKeys: String, CodingKey {
        case code
        case name
        case enable
        case busy
        case currency
        case timeZone = "time_zone"
        case languageCode = "language_code"
        case countryCode = "country_code"
        case workingArea = "working_area"
    }
    
    init() {}
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        name = try container.decode(String.self, forKey: .name)
        code = try container.decode(String.self, forKey: .code)
        countryCode = try container.decodeIfPresent(String.self, forKey: .countryCode) ?? ""
        currency = try container.decodeIfPresent(String.self, forKey: .currency) ?? ""
        languageCode = try container.decodeIfPresent(String.self, forKey: .languageCode) ?? ""
        isEnable = try container.decodeIfPresent(Bool.self, forKey: .enable) ?? false
        isBusy = try container.decodeIfPresent(Bool.self, forKey: .busy) ?? false
        workingArea = try container.decode([String].self, forKey: .workingArea)
    }
}

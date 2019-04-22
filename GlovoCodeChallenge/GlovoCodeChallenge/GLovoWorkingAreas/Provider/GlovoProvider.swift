//
//  GlovoProvider.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/19/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import Foundation
import Moya

let googleApiKey = "AIzaSyBte03wJW3a63KunNd3bJyyBQx1RLZUSa0"
let glovoUrl = "http://localhost:3000/api"

enum GlovoProvider {
    case cityList
    case countryList
    case cityDetail(with: String)
}

extension GlovoProvider: TargetType {
    
    var baseURL: URL {
        guard let url = URL(string: glovoUrl) else {
            fatalError("baseURL could not be configured")
        }
        return url
    }
    
    var path: String {
        switch self {
        case .cityList:
            return "/cities/"
        case .countryList:
            return "/countries/"
        case .cityDetail(let code):
            return "/cities/" + code
        }
    }
    var method: Moya.Method {
        switch self {
        default:
            return .get
        }
    }
    
    var task: Task {
        var params: [String: String] = [:]
        switch self {
        default:
            params ["language"] = "en-US"
            return .requestParameters(parameters: params, encoding: URLEncoding.queryString)
        }
    }
    
    var headers: [String : String]? {
        return ["Content-type": "application/json"]
    }
    
    var sampleData: Data {
        return Data()
    }
    
}

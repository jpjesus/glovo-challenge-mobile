//
//  CountryListCell.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/21/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import Foundation
import SnapKit

class CountryListCell: UITableViewCell {
    
    private lazy var cityLabel = UILabel()
    private let leading = 10
    
    func buildCell(for city: City) {
        setLabel(city.name.capitalized)
    }
    
    private func setLabel(_ cityName: String) {
        self.addSubview(cityLabel)
        cityLabel.text = cityName
        cityLabel.snp.makeConstraints { make -> Void in
            make.leading.equalTo(leading)
            make.centerY.equalToSuperview()
        }
    }
}

extension UITableViewCell {
    class var identifier: String {
        return String(describing: self)
    }
}

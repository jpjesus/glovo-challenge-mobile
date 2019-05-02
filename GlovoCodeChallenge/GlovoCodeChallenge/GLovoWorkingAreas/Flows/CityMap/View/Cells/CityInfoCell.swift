//
//  CityInfoCell.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/27/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import Foundation
import SnapKit
import UIKit

class CityInfoCell: UITableViewCell {
    private lazy var infoLabel = UILabel()
    private let leading = 10
    
    func buildCell(for city: City) {
        setLabel(city)
    }
    
    private func setLabel(_ city: City) {
        let attributedString: NSMutableAttributedString =  NSMutableAttributedString()
        attributedString.append(setAttributeString("Name: ", info: city.name))
        attributedString.append(setAttributeString("\n Code: ", info: city.code))
        attributedString.append(setAttributeString("\n Country Code: ", info: city.countryCode))
        attributedString.append(setAttributeString("\n Currency: ", info: city.currency))
        attributedString.append(setAttributeString("\n Busy: ", info: city.isBusy.description))
        attributedString.append(setAttributeString("\n Enable: ", info: city.isEnable.description))
        attributedString.append(setAttributeString("\n Language: ", info: city.languageCode))
        attributedString.append(setAttributeString("\n Time Zone: ", info: city.timeZone))
        infoLabel.attributedText = attributedString
        infoLabel.numberOfLines = 0
        self.addSubview(infoLabel)
        infoLabel.snp.makeConstraints { make -> Void in
            make.leading.equalToSuperview().inset(leading)
            make.centerY.equalToSuperview().offset(30)
        }
    }
    
    private func setAttributeString(_ title: String, info: String) -> NSMutableAttributedString {
        let attrs1 = [NSAttributedString.Key.font : UIFont.systemFont(ofSize: 14), NSAttributedString.Key.foregroundColor : UIColor.gray]
        let attrs2 = [NSAttributedString.Key.font : UIFont.systemFont(ofSize: 14), NSAttributedString.Key.foregroundColor : UIColor.black]
        
        let attributedString1 = NSMutableAttributedString(string: title, attributes:attrs1)
        let attributedString2 = NSMutableAttributedString(string: info, attributes:attrs2)
        attributedString1.append(attributedString2)
        return attributedString1
    }
}

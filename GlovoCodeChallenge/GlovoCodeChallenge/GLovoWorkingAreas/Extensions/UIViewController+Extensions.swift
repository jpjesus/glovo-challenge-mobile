//
//  UIViewController+Extensions.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/19/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import UIKit

extension UIViewController {
    
    func showAlertGoToSettings() {
        let alertGoSettings = UIAlertController(title: "Location permissions",
                                                message: "To enter with your current location we need location permissions",
                                                preferredStyle: .alert)
        
        let tryAgainAction = UIAlertAction(title: "Go to settings", style: .default, handler: { [weak self] _ in
            self?.openSettings()
        })
        
        let cancelAction = UIAlertAction(title: "Cancel", style: .destructive, handler: nil)
        
        alertGoSettings.addAction(tryAgainAction)
        alertGoSettings.addAction(cancelAction)
        self.present(alertGoSettings, animated: true, completion: nil)
    }
    
    func openSettings() {
        guard let settingsURL = UIApplication.openSettingsURLString.url else {
            return
        }
        
        openApplication(settingsURL)
    }
    
    func openApplication(_ url: URL, with options:[UIApplication.OpenExternalURLOptionsKey: Any] = [:], onCompleted complete: ((Bool) -> Void)? = nil) {
        guard UIApplication.shared.canOpenURL(url) else {
            return
        }
        
        UIApplication.shared.open(url, options: options, completionHandler: complete)
    }
}


public extension String {
    public var url: URL? {
        do {
            return try asURL()
        } catch _ {
            return nil
        }
    }
}

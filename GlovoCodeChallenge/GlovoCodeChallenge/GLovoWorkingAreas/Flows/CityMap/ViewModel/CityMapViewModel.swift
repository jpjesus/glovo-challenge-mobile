//
//  CityMapViewModel.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/27/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import Foundation
import Moya
import RxSwift
import RxCocoa
import GoogleMaps
import RxDataSources

class CityMapViewModel {
    
    private var disposeBag = DisposeBag()
    private let provider: MoyaProvider<GlovoProvider> = MoyaProvider<GlovoProvider>()
    var cityInfo: Driver<City>?
    var countries: Driver<[Country]>?
    var city: City?
    private weak var coordinator: CityMapCoordinator?
    
    init(_ coordinator: CityMapCoordinator, city: City?, currentLocation: CLLocationCoordinate2D?) {
        self.coordinator = coordinator
        if let _ = currentLocation {
            countries = setCountryWithCities().asDriver(onErrorJustReturn: [])
        } else {
            self.city = city
            cityInfo = getCityDetails().asDriver(onErrorJustReturn: City())
        }
    }
    
    func getCityDetails() -> Single<City> {
        return provider.rx
            .request(GlovoProvider.cityDetail(with: city?.code ?? ""))
            .map(City.self)
    }
    
    func getCountries() -> Single<[Country]> {
        return provider.rx
            .request(GlovoProvider.countryList)
            .debug()
            .map([Country].self)
    }
    
    func getCities() -> Single<[City]> {
        return provider.rx
            .request(GlovoProvider.cityList)
            .debug()
            .map([City].self)
        
    }
    
    func setCountryWithCities() -> Single<[Country]> {
        return Single.zip(getCountries(), getCities()) { (countries, cities) -> [Country] in
            let sortedCountries = countries.sorted(by: {$0.name < $1.name })
            let sortedCities = cities.sorted(by: {$0.name < $1.name })
            
            var countries: [Country] = []
            
            for country in sortedCountries {
                let cities = sortedCities.filter({ $0.countryCode == country.code })
                let entity = Country(name: country.name, code: country.code, cities: cities)
                countries.append(entity)
            }
            
            return countries
        }
    }
    
}

// MARK: Funcionts for city case
extension CityMapViewModel {
    
    func setCityInfo(_ city: City) -> [CityMapSection] {
        var results: [CityMapSection] = []
        self.city = city
        results.append(CityMapSection(header: "City Information", items: [city]))
        return results
    }
    
    func getCityMarker(_ city: City) -> GMSMarker {
        let coordinates = getCoordinatesForCity(city)
        return GMSMarker(position: coordinates)
    }
    
    func getCoordinatesForCity(_ city: City) -> CLLocationCoordinate2D {
        if let workingArea = city.workingArea.first,
            let path = GMSPath(fromEncodedPath: workingArea),
            !city.workingArea.isEmpty {
            return path.coordinate(at: 0)
        }
        return kCLLocationCoordinate2DInvalid
    }
    
    func getWorkingAreas(_ workingArea: [String]) -> [GMSPath] {
        return workingArea.compactMap{ GMSPath(fromEncodedPath: $0)}
    }
    
    func isLocationInMapBounds(_ currentLocation: CLLocationCoordinate2D, bounds: [String: GMSCoordinateBounds]) -> Bool {
        return bounds.contains{ $0.value.contains(currentLocation)}
    }
    
    
    func getCityBounds(for workingAreaPaths: [GMSPath]) -> GMSCoordinateBounds {
        var coordinateBounds = GMSCoordinateBounds()
        workingAreaPaths.filter { $0.count() > 0 }
            .forEach {
                for index in 0..<$0.count() {
                    coordinateBounds = coordinateBounds.includingCoordinate($0.coordinate(at: index))
                }
        }
        return coordinateBounds
    }
}

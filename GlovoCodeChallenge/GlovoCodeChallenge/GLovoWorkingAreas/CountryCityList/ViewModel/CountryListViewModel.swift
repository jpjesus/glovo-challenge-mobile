//
//  CountryListViewModel.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/19/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import Foundation
import RxCocoa
import RxSwift
import Moya
import RxDataSources
class CountryListViewModel {
    
    private var disposeBag = DisposeBag()
    private let provider: MoyaProvider<GlovoProvider> = MoyaProvider<GlovoProvider>()
    var countries: Driver<[Country]>?
    weak var coordinator: CountryListCoordinator?
    
    init(_ coordinator: CountryListCoordinator) {
        countries = setCountryWithCities().asDriver(onErrorJustReturn: [])
        self.coordinator = coordinator
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
    
    func setCountrySection(countries: [Country]) -> [CountrySection] {
        var results: [CountrySection] = []
        for country in countries {
            results.append(CountrySection(header: country.name, items: country.cities))
        }
        return results
    }

}

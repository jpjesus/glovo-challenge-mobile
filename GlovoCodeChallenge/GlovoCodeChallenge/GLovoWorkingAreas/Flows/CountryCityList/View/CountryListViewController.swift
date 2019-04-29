//
//  CountryListViewController.swift
//  GlovoCodeChallenge
//
//  Created by Jesus on 4/21/19.
//  Copyright © 2019 Jesus Paraada. All rights reserved.
//

import Foundation
import RxSwift
import RxCocoa
import RxDataSources
import SnapKit

class CountryListViewController: UIViewController {
    
    @IBOutlet weak var countryTableView: UITableView! {
        didSet {
            countryTableView.register(CountryListCell.self, forCellReuseIdentifier: CountryListCell.identifier)
        }
    }
    
    private var viewModel: CountryListViewModel
    private var disposeBag = DisposeBag()
    
    init(_ viewModel: CountryListViewModel) {
        self.viewModel = viewModel
        super.init(nibName: "CountryListViewController", bundle: nil)
    }
    
    required init?(coder aDecoder: NSCoder) {
        return nil
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUI()
        bind()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(false, animated: true)
        navigationController?.navigationItem.hidesBackButton = false
    }
}

extension CountryListViewController {
    
    private func setUI() {
        countryTableView.snp.makeConstraints { make ->  Void in
            make.bottom.top.trailing.leading.equalTo(0)
        }
    }
    
    private func bind() {
        let dataSource = createDataSource()
        
        viewModel.countries?
            .map(viewModel.setCountrySection)
            .drive(countryTableView.rx.items(dataSource: dataSource))
            .disposed(by: disposeBag)
        
        dataSource.titleForHeaderInSection = { dataSource, index in
            return dataSource.sectionModels[index].header
        }
        
        countryTableView.rx
            .setDelegate(self)
            .disposed(by: disposeBag)
        
        countryTableView.rx
            .modelSelected(City.self)
            .subscribe(onNext: { [unowned self] city in
                self.viewModel.goToMapView(with: city)
            }).disposed(by: disposeBag)
        
    }
    
    func createDataSource() -> RxTableViewSectionedReloadDataSource<CountrySection> {
        let dataSource = RxTableViewSectionedReloadDataSource<CountrySection>(configureCell: { (dataSource: TableViewSectionedDataSource<CountrySection>, tableView: UITableView, indexPath: IndexPath, item: City) in
            guard let cell: CountryListCell = self.countryTableView.dequeueReusableCell(withIdentifier: CountryListCell.identifier) as? CountryListCell else {
                fatalError("Couldnt find reference for CountryListCell")
            }
            cell.buildCell(for: item)
            return cell
        })
        return dataSource
    }
    
}

extension CountryListViewController: UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        return CGSize(width: collectionView.contentSize.width, height: 100)
        
    }
    
    func collectionView(_ collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                        insetForSectionAt section: Int) -> UIEdgeInsets {
        
        return UIEdgeInsets(top:0, left: 0, bottom: 0, right: 0)
    }
}

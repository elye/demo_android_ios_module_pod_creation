//
//  ViewController.swift
//  NetworkExperiment
//

import Alamofire
import SwiftyJSON
import UIKit
import WikipediaFetcher

class ViewController: UIViewController {

    @IBOutlet var searchEdit: UITextField!
    @IBOutlet var resultLabel: UILabel!
    @IBOutlet var indicator: UIActivityIndicatorView!

    private var request: DataRequest? = nil
    private lazy var wikipediaFetcher = WikipediaFetcher(
        onResponse: self.stopIndicationAnimation,
        showResult: self.showResult
    )

    @IBAction func beginSearch(_ sender: Any) {
        guard let searchText = searchEdit.text, !searchText.isEmpty else {
            return
        }

        resultLabel.text = ""
        indicator.startAnimating()
        fetchData(searchText: searchText)
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        indicator.hidesWhenStopped = true
    }

    override func viewDidDisappear(_ animated: Bool) {
        request?.cancel()
    }

    private func fetchData(searchText: String) {
        request?.cancel()
        request = wikipediaFetcher.fetchData(searchText: searchText)
    }

    private func stopIndicationAnimation() {
        self.indicator.stopAnimating()
    }

    private func showResult(data: String) {
        resultLabel.text = data
    }
}

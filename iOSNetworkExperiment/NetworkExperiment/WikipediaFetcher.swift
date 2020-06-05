//
//  WikipediaFetcher.swift
//  NetworkExperiment
//

import Alamofire
import SwiftyJSON
import Fetcher

class WikipediaFetcher {
    private static let url = "https://en.wikipedia.org/w/api.php"
    private let fetcher = Fetcher()

    private let onResponse: () -> ()
    private let showResult: (String) -> ()

    init (onResponse: @escaping () -> (),
          showResult: @escaping (String) -> ()) {
        self.onResponse = onResponse
        self.showResult = showResult
    }

    public func fetchData(searchText: String)
        -> DataRequest {
            
            let parameters: Parameters = [
                "action": "query",
                "format": "json",
                "list": "search",
                "srsearch": searchText
            ]

            let request = fetcher.fetch(url: WikipediaFetcher.url,
                                        parameters: parameters,
                                        onResponse: self.onResponse,
                                        onSuccess: self.onSuccess,
                                        onFailure: self.onError)

            return request
    }

    private func onSuccess(data: Any) {
        let swiftyJsonVar = JSON(data)
        let countObj = swiftyJsonVar["query"]["searchinfo"]["totalhits"]
        guard let count = countObj.int else {
            self.showResult("No data found")
            return
        }
        self.showResult("Count is \(count)")

    }

    private func onError(message: String) {
        self.showResult(message)
    }
}

//
//  Fetcher.swift
//  NetworkExperiment
//

import Alamofire

class Fetcher {

    func fetch(url: String,
               parameters: Parameters,
               onResponse: @escaping () -> (),
               onSuccess: @escaping (Any) -> (),
               onFailure: @escaping (String) -> ())
        -> DataRequest {

            let request = Alamofire.SessionManager.default.request(url, method: .get, parameters: parameters)
                .responseData { (resData) -> Void in
                    onResponse()
                    switch (resData.result) {
                    case let .success(data):
                        onSuccess(data)
                    case let .failure(error):
                        onFailure(error.localizedDescription)
                    }
            }

            return request
    }
}

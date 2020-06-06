package com.elyeproj.wikipediafetcher

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import com.elyeproj.fetcher.Fetcher
import okhttp3.Interceptor

class WikipediaFetcher(userAgentInterceptor: Interceptor) {

    object Model {
        data class Result(val query: Query)
        data class Query(val searchinfo: SearchInfo)
        data class SearchInfo(val totalhits: Int)
    }

    private val fetcher by lazy {
        Fetcher(userAgentInterceptor)
    }

    private val httpUrlBuilder by lazy {
        HttpUrl.Builder()
            .scheme("https")
            .host("en.wikipedia.org")
            .addPathSegment("w")
            .addPathSegment("api.php")
            .addQueryParameter("action", "query")
            .addQueryParameter("format", "json")
            .addQueryParameter("list", "search")
    }

    fun performFetch(
        searchText: String,
        onPerformFetch: () -> Unit,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ): Disposable {
        onPerformFetch()
        val httpUrl = httpUrlBuilder.addQueryParameter("srsearch", searchText).build()

        return Single.just(searchText)
            .map { fetcher.fetchOnBackground(httpUrl, ::onSuccess) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess("Count is $it") },
                { onFailure(it.localizedMessage) })
    }

    private fun onSuccess(raw: String?): String {
        return try {
            val result = Gson().fromJson(raw, Model.Result::class.java)
            result.query.searchinfo.totalhits.toString()
        } catch (exception: JsonSyntaxException) {
            "No data found "
        }
    }
}
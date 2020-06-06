package com.elyeproj.networkexperiment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.elyeproj.wikipediafetcher.WikipediaFetcher
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var disposable = Disposables.disposed()
    private val wikipediaFetcher by lazy {
        WikipediaFetcher(UserAgentInterceptor(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun beginSearch(view: View) {
        if (searchText.text.isNotBlank()) {
            disposable = wikipediaFetcher.performFetch(
                searchText.text.toString(),
                ::onPerformFetch, ::showResult, ::showResult
            )
        }
    }

    private fun onPerformFetch() {
        resultText.text = ""
        progressIndicator.visibility = View.VISIBLE
        disposable.dispose()
    }

    private fun showResult(result: String) {
        progressIndicator.visibility = View.GONE
        resultText.text = result
    }

    override fun onPause() {
        disposable.dispose()
        super.onPause()
    }
}

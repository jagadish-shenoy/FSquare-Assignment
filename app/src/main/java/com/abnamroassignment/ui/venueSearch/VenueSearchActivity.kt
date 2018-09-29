package com.abnamroassignment.ui.venueSearch

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.abnamroassignment.R
import com.abnamroassignment.foreaquare.Status
import com.abnamroassignment.foreaquare.Venue
import com.abnamroassignment.foreaquare.VenueDetailsResult
import com.abnamroassignment.foreaquare.VenueSearchResult
import com.abnamroassignment.search.VenueSearchViewModel
import com.abnamroassignment.ui.venueDetails.VenueDetailsActivity
import com.abnamroassignment.ui.venueSearch.VenueSearchResultAdapter.VenueItemClickCallback
import kotlinx.android.synthetic.main.activity_venue_search.*


@Suppress("NON_EXHAUSTIVE_WHEN")
class VenueSearchActivity : AppCompatActivity(), VenueItemClickCallback {

    private val adapter: VenueSearchResultAdapter = VenueSearchResultAdapter(this)

    private lateinit var viewModel: VenueSearchViewModel

    private lateinit var imm: InputMethodManager

    private val searchResultHandler = Observer<VenueSearchResult> { searchResult: VenueSearchResult? ->
        searchResult?.apply {
            when (status) {
                Status.INVALID_REQUEST -> showSnackbar(R.string.error_invalid_search_request)

                Status.NETWORK_ERROR -> showSnackbar(R.string.error_network_search_reqeust)
            }
            adapter.venues = venues
        }
    }

    private val venueDetailsHandler = Observer<VenueDetailsResult> {
        it?.apply {
            when (status) {
                Status.INVALID_REQUEST -> showSnackbar(R.string.error_fetching_venue_details)

                Status.NETWORK_ERROR -> showSnackbar(R.string.error_network_venue_details)

                Status.SUCCESS -> VenueDetailsActivity.start(venueDetails!!, this@VenueSearchActivity)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_search)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        viewModel = ViewModelProviders.of(this).get(VenueSearchViewModel::class.java)

        venueSearchResultsView.layoutManager = LinearLayoutManager(this)
        venueSearchResultsView.adapter = adapter

        viewModel.venueSearchResultLiveData.observe(this, searchResultHandler)
        viewModel.venueDetailsResultLiveData.observe(this, venueDetailsHandler)

        searchLocation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }
        searchButton.setOnClickListener { performSearch() }
    }

    override fun onVenueItemClicked(venue: Venue) {
        viewModel.foreSquareManager.fetchVenueDetails(venue)
    }

    private fun performSearch() {
        val location = searchLocation.text.toString()
        if (!location.isEmpty()) {
            imm.hideSoftInputFromWindow(searchResultContainer.windowToken, 0)
            viewModel.foreSquareManager.searchVenues(location)
        }
    }

    private fun showSnackbar(message: Int) {
        Snackbar.make(searchResultContainer, message, Snackbar.LENGTH_LONG).show()
    }
}
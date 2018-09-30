package com.abnamroassignment.ui.venueSearch

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.abnamroassignment.R
import com.abnamroassignment.foreaquare.Status
import com.abnamroassignment.foreaquare.Venue
import com.abnamroassignment.foreaquare.VenueDetailsResult
import com.abnamroassignment.foreaquare.VenueSearchResult
import com.abnamroassignment.ui.venueSearch.VenueSearchResultAdapter.VenueItemClickCallback
import com.abnamroassignment.ui.venueSearch.viewmodel.VenueSearchViewModel

class VenueSearchPresenter(private val venueSearchView: VenueSearchView,
                           private val viewModel:VenueSearchViewModel,
                           lifecycleOwner: LifecycleOwner): VenueItemClickCallback {

    private val searchResultHandler = Observer<VenueSearchResult> { searchResult: VenueSearchResult? ->
        searchResult?.apply {
            when (status) {
                Status.INVALID_REQUEST -> venueSearchView.showSnackbar(R.string.error_invalid_search_request)

                Status.NETWORK_ERROR,
                Status.CACHE_MISS -> venueSearchView.showSnackbar(R.string.error_network_search_reqeust)

                Status.SUCCESS -> if (searchResult.venues.isEmpty()) {
                    venueSearchView.showSnackbar(R.string.warning_no_venues_found)
                }
            }
            venueSearchView.setAdapterItems(venues)
        }
    }

    private val venueDetailsHandler = Observer<VenueDetailsResult> {
        it?.apply {
            when (status) {
                Status.INVALID_REQUEST -> venueSearchView.showSnackbar(R.string.error_fetching_venue_details)

                Status.NETWORK_ERROR,
                Status.CACHE_MISS -> venueSearchView.showSnackbar(R.string.error_network_venue_details)

                Status.SUCCESS -> venueSearchView.startVenueDetailsActivity(venueDetails!!)
            }
        }
    }

    init {
        viewModel.venueSearchResultLiveData.observe(lifecycleOwner, searchResultHandler)
        viewModel.venueDetailsResultLiveData.observe(lifecycleOwner, venueDetailsHandler)
    }

    override fun onVenueItemClicked(venue:Venue) {
        viewModel.foreSquareManager.fetchVenueDetails(venue)
    }

    internal fun onSearchClicked() {
        val location = venueSearchView.getLocation()
        if (!location.isEmpty()) {
            venueSearchView.hideKeyboard()
            viewModel.foreSquareManager.searchVenues(location)
        }
    }
}
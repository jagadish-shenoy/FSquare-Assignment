package com.abnamroassignment.search

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.abnamroassignment.foreaquare.VenueDetailsResult
import com.abnamroassignment.foreaquare.VenueSearchResult

class VenueSearchViewModel(context:Application):AndroidViewModel(context), FourSquareRequester.VenueCallback {

    val foreaquareRequestor = FourSquareRequester(this)

    val venueSearchResultLiveData = MutableLiveData<VenueSearchResult>()

    val venueDetailsResultLiveData = MutableLiveData<VenueDetailsResult>()

    override fun onVenueSearchResponse(venueSearchResult: VenueSearchResult) {
        venueSearchResultLiveData.postValue(venueSearchResult)
    }

    override fun onVenueDetailsResponse(venueDetailsResult: VenueDetailsResult) {
        venueDetailsResultLiveData.postValue(venueDetailsResult)
    }
}
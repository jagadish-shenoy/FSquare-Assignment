package com.abnamroassignment.ui.venueSearch

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.abnamroassignment.R
import com.abnamroassignment.foreaquare.*
import com.abnamroassignment.ui.venueSearch.viewmodel.VenueSearchViewModel
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class VenueSearchPresenterTest {

    @Rule
    @JvmField
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var viewModel: VenueSearchViewModel

    @Mock
    private lateinit var searchResultLiveData: MutableLiveData<VenueSearchResult>

    @Mock
    private lateinit var venueDetailsLiveData: MutableLiveData<VenueDetailsResult>

    @Mock
    private lateinit var view: VenueSearchView

    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    private lateinit var fourSquareManager: FourSquareManager

    @Before
    fun setup() {
        whenever(viewModel.venueSearchResultLiveData).thenReturn(searchResultLiveData)
        whenever(viewModel.venueDetailsResultLiveData).thenReturn(venueDetailsLiveData)
        whenever(viewModel.foreSquareManager).thenReturn(fourSquareManager)
    }

    @Test
    fun `register with search result live data on creation`() {
        VenueSearchPresenter(view, viewModel, lifecycleOwner)
        verify(searchResultLiveData).observe(eq(lifecycleOwner), any<Observer<VenueSearchResult>>())
    }

    @Test
    fun `register with venue details live data for on creation`() {
        VenueSearchPresenter(view, viewModel, lifecycleOwner)
        verify(venueDetailsLiveData).observe(eq(lifecycleOwner), any<Observer<VenueDetailsResult>>())
    }

    @Test
    fun `fetch location from View on onSearchClicked`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        whenever(view.getLocation()).thenReturn("")
        presenter.onSearchClicked()

        verify(view).getLocation()
    }

    @Test
    fun `not hide keyboard if location is empty`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        whenever(view.getLocation()).thenReturn("")
        presenter.onSearchClicked()

        verify(view, never()).hideKeyboard()
    }

    @Test
    fun `hide keyboard if location is not empty`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        whenever(view.getLocation()).thenReturn("test")
        presenter.onSearchClicked()

        verify(view).hideKeyboard()
    }

    @Test
    fun `request FourSquareManager searchVenues if location not empty`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        whenever(view.getLocation()).thenReturn("test")
        presenter.onSearchClicked()

        verify(fourSquareManager).searchVenues("test")
    }

    @Test
    fun `not request FourSquareManager searchVenues if location is empty`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        whenever(view.getLocation()).thenReturn("")
        presenter.onSearchClicked()

        verify(fourSquareManager, never()).searchVenues(anyString())
    }

    @Test
    fun `show invalid request error for search result`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        presenter.handleSearchResult(VenueSearchResult("", Status.INVALID_REQUEST, emptyList()))

        verify(view).showSnackbar(R.string.error_invalid_search_request)
    }

    @Test
    fun `show network error for search result`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        presenter.handleSearchResult(VenueSearchResult("", Status.NETWORK_ERROR, emptyList()))

        verify(view).showSnackbar(R.string.error_network_search_reqeust)
    }

    @Test
    fun `show network error in case of cache miss for search result`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        presenter.handleSearchResult(VenueSearchResult("", Status.NETWORK_ERROR, emptyList()))
        verify(view).showSnackbar(R.string.error_network_search_reqeust)
    }

    @Test
    fun `show no results warning for success search result with no venues`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        presenter.handleSearchResult(VenueSearchResult("", Status.SUCCESS, emptyList()))
        verify(view).showSnackbar(R.string.warning_no_venues_found)
    }

    @Test
    fun `call setAdapterItems in case of invalid request for search result`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        presenter.handleSearchResult(VenueSearchResult("", Status.INVALID_REQUEST, emptyList()))

        verify(view).setAdapterItems(emptyList())
    }

    @Test
    fun `call setAdapterItems in case of network error for search result`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        presenter.handleSearchResult(VenueSearchResult("", Status.NETWORK_ERROR, emptyList()))

        verify(view).setAdapterItems(emptyList())
    }

    @Test
    fun `call setAdapterItems in case of cache miss for search result`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        presenter.handleSearchResult(VenueSearchResult("", Status.CACHE_MISS, emptyList()))

        verify(view).setAdapterItems(emptyList())
    }

    @Test
    fun `call setAdapterItems in case of empty search result`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        presenter.handleSearchResult(VenueSearchResult("", Status.SUCCESS, emptyList()))

        verify(view).setAdapterItems(emptyList())
    }

    @Test
    fun `call setAdapterItems in case of search result success with venues`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        presenter.handleSearchResult(VenueSearchResult("", Status.SUCCESS, listOf(Venue("1", "test", "test"))))
        verify(view).setAdapterItems(listOf(Venue("1", "test", "test")))
    }

    @Test
    fun `show venue fetch failed in case of venue details invalid request`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        presenter.handleVenueResult(VenueDetailsResult("", null, Status.INVALID_REQUEST))

        verify(view).showSnackbar(R.string.error_fetching_venue_details)
    }

    @Test
    fun `show network error in case of venue details request network error`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        presenter.handleVenueResult(VenueDetailsResult("", null, Status.NETWORK_ERROR))

        verify(view).showSnackbar(R.string.error_network_venue_details)
    }

    @Test
    fun `show network error in case of venue details request cache miss`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        presenter.handleVenueResult(VenueDetailsResult("", null, Status.CACHE_MISS))

        verify(view).showSnackbar(R.string.error_network_venue_details)
    }

    @Test
    fun `start venue details activity if details fetch success`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)
        presenter.handleVenueResult(VenueDetailsResult("",
                VenueDetails("", "", "", "", "", "", ""),
                Status.SUCCESS))

        verify(view).startVenueDetailsActivity(VenueDetails("", "", "", "", "", "", ""))
    }

    @Test
    fun `request FourSquareManager for venue details on click`() {
        val presenter = VenueSearchPresenter(view, viewModel, lifecycleOwner)

        val venue = Venue("", "", "")
        presenter.onVenueItemClicked(venue)

        verify(fourSquareManager).fetchVenueDetails(venue)
    }


}
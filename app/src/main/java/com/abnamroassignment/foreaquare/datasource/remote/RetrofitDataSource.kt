package com.abnamroassignment.foreaquare.datasource.remote

import android.content.Context
import com.abnamroassignment.BuildConfig
import com.abnamroassignment.foreaquare.*
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitDataSource(context: Context) : DataSource(context) {

    private val venueService: VenueService = createVenueService()

    override fun searchVenues(location: String, limit: Int) {
        venueService.searchVenues(location, VENUE_SEARCH_RADIUS, limit).enqueue(
                object : retrofit2.Callback<VenueSearchResult> {

                    override fun onFailure(call: Call<VenueSearchResult>?, t: Throwable?) {
                        callback?.onVenueSearchResponse(this@RetrofitDataSource, NetworkErrorSearchResult)
                    }

                    override fun onResponse(call: Call<VenueSearchResult>?, httpResponse: Response<VenueSearchResult>?) {
                        if(httpResponse == null || !httpResponse.isSuccessful || httpResponse.body() == null) {
                            callback?.onVenueSearchResponse(this@RetrofitDataSource, InvalidRequestSearchResult)
                        } else {
                            callback?.onVenueSearchResponse(this@RetrofitDataSource, httpResponse.body()!!)
                        }
                    }
                }
        )
    }

    override fun fetchVenueDetails(venueId: String) {
        venueService.getVenueDetails(venueId).enqueue(
                object : retrofit2.Callback<VenueDetailsResult> {
                    override fun onFailure(call: Call<VenueDetailsResult>?, t: Throwable?) {
                        callback?.onVenueDetailsResponse(this@RetrofitDataSource, NetworkErrorVenueDetailsResult)
                    }

                    override fun onResponse(call: Call<VenueDetailsResult>?, httpResponse: Response<VenueDetailsResult>?) {
                        if (httpResponse == null || !httpResponse.isSuccessful || httpResponse.body() == null) {
                            callback?.onVenueDetailsResponse(this@RetrofitDataSource, InvalidRequestVenueDetailsResult)
                        } else {
                            callback?.onVenueDetailsResponse(this@RetrofitDataSource, httpResponse.body()!!)
                        }
                    }
                }
        )
    }

    private fun createVenueService(): VenueService {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_FOURSQUARE)
                .addConverterFactory(createGsonConverter())
                .client(createClientWithDefaultParams())
                .build()
        return retrofit.create(VenueService::class.java)
    }

    private fun createGsonConverter() = GsonConverterFactory.create(GsonBuilder()
            .registerTypeAdapter(VenueSearchResult::class.java, VenueSearchResultTypeAdapter())
            .registerTypeAdapter(VenueDetailsResult::class.java, VenueDetailsResultTypeAdapter())
            .create())

    private fun createClientWithDefaultParams(): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()

        httpClientBuilder.addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                    .addQueryParameter(KEY_CLIENT_ID, BuildConfig.FSQUARE_CLIENT_ID)
                    .addQueryParameter(KEY_CLIENT_SECRET, BuildConfig.FSQUARE_CLIENT_SECRET)
                    .addQueryParameter(KEY_VERSION, BuildConfig.FSQUARE_API_VERSION)
                    .build()

            // Request customization: add request headers
            val requestBuilder = original.newBuilder().url(url)
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        return httpClientBuilder.build()

    }
}
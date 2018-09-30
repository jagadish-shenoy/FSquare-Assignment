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
                object : retrofit2.Callback<Venues> {

                    override fun onFailure(call: Call<Venues>?, t: Throwable?) {
                        callback?.onVenueSearchResponse(this@RetrofitDataSource,
                                createNetworkErrorSearchResult(location))
                    }

                    override fun onResponse(call: Call<Venues>?, httpResponse: Response<Venues>?) {
                        if(httpResponse == null || !httpResponse.isSuccessful || httpResponse.body() == null) {
                            callback?.onVenueSearchResponse(this@RetrofitDataSource,
                                    createInvalidRequestSearchResult(location))
                        } else {
                            callback?.onVenueSearchResponse(this@RetrofitDataSource,
                                    VenueSearchResult(location, Status.SUCCESS, httpResponse.body()!!.list))
                        }
                    }
                }
        )
    }

    override fun fetchVenueDetails(venueId: String) {
        venueService.getVenueDetails(venueId).enqueue(
                object : retrofit2.Callback<VenueDetails> {
                    override fun onFailure(call: Call<VenueDetails>, t: Throwable?) {
                        callback?.onVenueDetailsResponse(this@RetrofitDataSource,
                                createNetworkErrorVenueDetailsResult(venueId))
                    }

                    override fun onResponse(call: Call<VenueDetails>, httpResponse: Response<VenueDetails>) {
                        val venueDetailsResult = extractVenueDetailsResult(venueId, httpResponse)
                        callback?.onVenueDetailsResponse(this@RetrofitDataSource, venueDetailsResult)
                    }
                }
        )
    }

    override fun fetchVenueDetailsSync(venueId: String): VenueDetailsResult {
        val response = venueService.getVenueDetails(venueId).execute()
        return extractVenueDetailsResult(venueId, response)
    }

    private fun extractVenueDetailsResult(venueId: String, httpResponse: Response<VenueDetails>): VenueDetailsResult {
        return if (!httpResponse.isSuccessful || httpResponse.body() == null) {
            createInvalidRequestVenueDetailsResult(venueId)
        } else {
            VenueDetailsResult(venueId, httpResponse.body()!!, Status.SUCCESS)
        }
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
            .registerTypeAdapter(Venues::class.java, VenueSearchResultTypeAdapter())
            .registerTypeAdapter(VenueDetails::class.java, VenueDetailsResultTypeAdapter())
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
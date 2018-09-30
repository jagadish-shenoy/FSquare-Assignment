package com.abnamroassignment.foreaquare.datasource.remote

import android.content.Context
import com.abnamroassignment.BuildConfig
import com.abnamroassignment.foreaquare.*
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitDataSource(context: Context) : DataSource(context) {

    private val venueService: VenueService = createVenueService()

    override fun searchVenues(location: String, limit: Int): VenueSearchResult {
        return try {
            val response = venueService.searchVenues(location, VENUE_SEARCH_RADIUS, limit).execute()
            if (response == null || !response.isSuccessful || response.body() == null) {
                createInvalidRequestSearchResult(location)
            } else {
                VenueSearchResult(location, Status.SUCCESS, response.body()!!.list)
            }
        } catch (e: IOException) {
            createNetworkErrorSearchResult(location)
        }
    }

    override fun fetchVenueDetails(venueId: String): VenueDetailsResult {
        val response = venueService.getVenueDetails(venueId).execute()
        return extractVenueDetailsResult(venueId, response)
    }

    private fun extractVenueDetailsResult(venueId: String, httpResponse: Response<VenueDetails>): VenueDetailsResult {
        return try {
            if (!httpResponse.isSuccessful || httpResponse.body() == null) {
                createInvalidRequestVenueDetailsResult(venueId)
            } else {
                VenueDetailsResult(venueId, httpResponse.body()!!, Status.SUCCESS)
            }
        } catch (e: IOException) {
            createNetworkErrorVenueDetailsResult(venueId)
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

        httpClientBuilder.connectTimeout(10, TimeUnit.SECONDS)
        httpClientBuilder.readTimeout(10, TimeUnit.SECONDS)

        return httpClientBuilder.build()
    }
}
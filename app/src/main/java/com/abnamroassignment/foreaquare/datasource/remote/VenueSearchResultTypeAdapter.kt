package com.abnamroassignment.foreaquare.datasource.remote

import com.abnamroassignment.foreaquare.Status
import com.abnamroassignment.foreaquare.Venue
import com.abnamroassignment.foreaquare.VenueSearchResult
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class VenueSearchResultTypeAdapter : JsonDeserializer<VenueSearchResult> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): VenueSearchResult {

        val fullResponseJson = json.asJsonObject

        val venueResponseJson = fullResponseJson.getAsJsonObject("response")

        val venuesJson = venueResponseJson.getAsJsonArray("venues")

        val venueList = venuesJson.map {
            val venueJson = it.asJsonObject

            Venue(venueJson.get("id").asString,
                    venueJson.get("name").asString,
                    extractAddressFromLocation(venueJson.get("location").asJsonObject))
        }.toList()

        return VenueSearchResult(Status.SUCCESS, venueList)
    }

    private fun extractAddressFromLocation(venueJson: JsonObject?) =
            venueJson?.let {
                val formattedAddress = it.get("formattedAddress").asJsonArray
                formattedAddress?.joinToString(separator = "\n") {
                    it.asString
                } ?: "N/A"
            } ?: "N/A"
}
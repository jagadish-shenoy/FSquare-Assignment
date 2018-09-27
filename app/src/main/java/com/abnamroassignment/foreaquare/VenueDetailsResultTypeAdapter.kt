package com.abnamroassignment.foreaquare

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class VenueDetailsResultTypeAdapter:JsonDeserializer<VenueDetailsResult> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): VenueDetailsResult {
        val fullResponseJson = json.asJsonObject

        val venueDetailsResponseJson = fullResponseJson.getAsJsonObject("response")

        val venueDetailsJson = venueDetailsResponseJson.getAsJsonObject("venue")

        return VenueDetailsResult(Status.SUCCESS,
                VenueDetails(
                        venueDetailsJson.get("id").asString,
                        venueDetailsJson.get("name").asString,
                        venueDetailsJson.get("description")?.asString ?: "N/A",
                        extractAddressFromLocation(venueDetailsJson),
                        extractPhoneContact(venueDetailsJson),
                        venueDetailsJson.get("rating")?.asString ?: "N/A"
                ))
    }

    private fun extractAddressFromLocation(venueJson: JsonObject) =
            venueJson.getAsJsonObject("location").getAsJsonArray("formattedAddress")?.asJsonArray?.joinToString(separator = "\n") ?: "N/A"

    private fun extractPhoneContact(venueJson: JsonObject) =
        venueJson.getAsJsonObject("contact")?.get("formattedPhone")?.asString?:"N/A"


}
package com.abnamroassignment.foreaquare.datasource.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

import com.abnamroassignment.foreaquare.Venue
import com.abnamroassignment.foreaquare.VenueDetails

@Database(entities = [(Venue::class), (VenueDetails::class)], version = 1)
abstract class VenueDatabase : RoomDatabase() {

    abstract fun venuesDao(): VenuesDao

    abstract fun venueDetailsDao(): VenueDetailsDao

    companion object {

        private var INSTANCE: VenueDatabase? = null

        fun getInMemoryDatabase(context: Context): VenueDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.inMemoryDatabaseBuilder(context.applicationContext, VenueDatabase::class.java)
                        // To simplify the codelab, allow queries on the main thread.
                        // Don't do this on a real app! See PersistenceBasicSample for an example.
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
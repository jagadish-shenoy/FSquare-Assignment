package com.abnamroassignment.foreaquare.datasource.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.abnamroassignment.foreaquare.VenueDetails
import com.abnamroassignment.foreaquare.VenueEntity

@Database(entities = [(VenueEntity::class), (VenueDetails::class)], version = 1)
abstract class VenueDatabase : RoomDatabase() {

    abstract fun venuesDao(): VenuesDao

    abstract fun venueDetailsDao(): VenueDetailsDao

    companion object {

        private var INSTANCE: VenueDatabase? = null

        fun getInMemoryDatabase(context: Context): VenueDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.inMemoryDatabaseBuilder(context.applicationContext, VenueDatabase::class.java)
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
package com.lightricks.feedexercise.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Entity::class], version = 1)
abstract class FeedDatabase : RoomDatabase() {

    abstract fun feedDao(): FeedDao

    companion object {
        private var INSTANCE: FeedDatabase? = null
        fun getDatabase(context: Context): FeedDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    FeedDatabase::class.java, "feed_table"
                ).build()
            }
            return INSTANCE as FeedDatabase
        }
    }
}
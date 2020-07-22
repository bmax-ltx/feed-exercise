package com.lightricks.feedexercise.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Entity::class), version = 1)
abstract class FeedDatabase : RoomDatabase() {

    abstract fun feedDao(): FeedDao

    companion object {
        fun getDatabase(context: Context): FeedDatabase {
            return Room.databaseBuilder(
                context,
                FeedDatabase::class.java, "feed_table"
            ).build()
        }
    }

}
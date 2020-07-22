package com.lightricks.feedexercise.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(ItemEntity::class), version = 1)
public abstract class FeedDatabase : RoomDatabase(){

    abstract fun feedDao(): FeedDao
    @Volatile
    private var INSTANCE: FeedDatabase? = null

    fun getDatabase(context: Context): FeedDatabase{
        val tempInstance = INSTANCE
        if (tempInstance != null){
            return tempInstance
        }
        synchronized(this){
            val instance = Room.databaseBuilder(
                context.applicationContext,
                FeedDatabase::class.java,
                "feed_table"
            ).build()
            INSTANCE  = instance
            return instance
        }
    }
}
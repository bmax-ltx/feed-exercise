package com.lightricks.feedexercise.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.lightricks.feedexercise.database.Entity
import com.lightricks.feedexercise.database.FeedDao
import com.lightricks.feedexercise.database.FeedDatabase

/**
 * This is our data layer abstraction. Users of this class don't need to know
 * where the data actually comes from (network, database or somewhere else).
 */
class FeedRepository(private val feedDao: FeedDao) {
    val database = FeedDatabase.getExistingDatabase()
    val allContents = database.feedDao().getAllItems()
    val feedItems: LiveData<List<FeedItem>> = Transformations.map(allContents) {//[1]
        it.toFeedItems()//[2] }
    }

    fun List<Entity>.toFeedItems(): List<FeedItem> { return map {
        FeedItem(it.id, it.thumbnailUrl!!, it.isPremium!!) }
    }
}
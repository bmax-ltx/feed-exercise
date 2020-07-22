package com.lightricks.feedexercise.data

import com.lightricks.feedexercise.database.FeedDao

/**
 * This is our data layer abstraction. Users of this class don't need to know
 * where the data actually comes from (network, database or somewhere else).
 */
class FeedRepository(private val feedDao: FeedDao) {
//
//    val allFeed: LiveData<List<ItemEntity>> = feedDao.getAllItems()
//
//
//    fun insert(items: List<ItemEntity>){
//        feedDao.insertList(items)
//    }

}
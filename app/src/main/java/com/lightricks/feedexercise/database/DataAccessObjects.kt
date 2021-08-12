package com.lightricks.feedexercise.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable

@Dao
interface FeedItemDao {
    @Insert
    fun insertAll(vararg items: FeedItemEntity): Completable // TODO should receive feedItem of feedItemEntity?

    @Delete
    fun deleteAll(): Completable

    @Query("SELECT * FROM FeedItemEntity")
    fun getAll(): LiveData<List<FeedItemEntity>> // TODO how does it know to return live data?

    @Query("SELECT COUNT(*) FROM FeedItemEntity")
    fun getCount(): Int
}


package com.lightricks.feedexercise.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable


/***
 * todo: add Room's Data Access Object interface(s) here
 */
@Dao
interface FeedDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: List<Entity>): Completable

    @Query("DELETE FROM feed_table")
    fun deleteAll(): Completable

    @Query("SELECT * FROM feed_table")
    fun getAllItems(): LiveData<List<Entity>>

    @Query("SELECT COUNT(*) FROM feed_table")
    fun getSize(): Int

}
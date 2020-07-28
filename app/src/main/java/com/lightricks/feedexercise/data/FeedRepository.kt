package com.lightricks.feedexercise.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.lightricks.feedexercise.database.Entity
import com.lightricks.feedexercise.database.FeedDao
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.FeedResponse
import io.reactivex.Completable

/**
 * This is our data layer abstraction. Users of this class don't need to know
 * where the data actually comes from (network, database or somewhere else).
 */
class FeedRepository(private val feedDao: FeedDao, private val feedApiService: FeedApiService) {

    fun getLiveData(): LiveData<List<FeedItem>> {
        val entityLiveData = feedDao.getAllItems()
        return Transformations.map(entityLiveData) {
            it.toFeedItems()
        }
    }

    fun List<Entity>.toFeedItems(): List<FeedItem> {
        return map {
            FeedItem(it.id, it.thumbnailUrl!!, it.isPremium!!)
        }
    }

    @SuppressLint("CheckResult")
    fun refresh(): Completable {
        val result = feedApiService.getMetadataList()
        return result.flatMapCompletable {
            val list = jsonToEntity(it)
            feedDao.insertList(list)
        }
    }

    private fun jsonToEntity(json: FeedResponse): List<Entity> {
        val ls = ArrayList<Entity>()
        val thumbnailUrlPrefix =
            "https://assets.swishvideoapp.com/Android/demo/catalog/thumbnails/"
        for (j in json.templatesMetadata) {
            val ent = Entity(j.id, thumbnailUrlPrefix + j.templateThumbnailURI, j.isPremium)
            ls.add(ent)
        }
        return ls
    }

}
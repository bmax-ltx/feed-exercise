package com.lightricks.feedexercise.data

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.database.FeedItemEntity
import com.lightricks.feedexercise.network.FeedApi
import com.lightricks.feedexercise.network.GetFeedResponse
import com.lightricks.feedexercise.network.TemplatesMetadataItem
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


private const val BASE_URL: String =
    "https://assets.swishvideoapp.com/Android/demo/catalog/thumbnails/"

/**
 * This is our data layer abstraction. Users of this class don't need to know
 * where the data actually comes from (network, database or somewhere else).
 */
class FeedRepository(
    private val feedApiService: FeedApi,
    private val feedDatabase: FeedDatabase
) {
    private val feedItems = MutableLiveData<List<FeedItem>>()

    fun getFeedItems(): LiveData<List<FeedItem>> = feedItems

    @SuppressLint("CheckResult")
    fun refresh(): Completable {
        return feedApiService.service.getFeed()
            .subscribeOn(Schedulers.io())
            .flatMapCompletable { feedResponse ->
                handleResponse(feedResponse)
            }
    }

    fun handleResponse(feedResponse: GetFeedResponse): Completable {
        val feedItemList: MutableList<FeedItem> = emptyList<FeedItem>().toMutableList()
        val feedItemEntityList: MutableList<FeedItemEntity> =
            emptyList<FeedItemEntity>().toMutableList()
        for (item in feedResponse.templatesMetadata) {
            feedItemList.add(templatesMetadataToFeedItem(item))
            feedItemEntityList.add(templatesMetadataToFeedItemEntity(item))
        }

        feedItems.value = feedItemList//not io
        return saveItemsToDB(feedItemEntityList)// io
    }

    /**
     * Convert templatesMetadataItem to FeedItem
     */
    private fun templatesMetadataToFeedItem(templatesMetadataItem: TemplatesMetadataItem): FeedItem {
        return FeedItem(
            templatesMetadataItem.id,
            BASE_URL + templatesMetadataItem.templateThumbnailURI,
            templatesMetadataItem.isPremium
        )
    }

    /**
     * Convert templatesMetadataItem to FeedItemEntity
     */
    private fun templatesMetadataToFeedItemEntity(templatesMetadataItem: TemplatesMetadataItem): FeedItemEntity {
        return FeedItemEntity(
            templatesMetadataItem.id,
            templatesMetadataItem.templateThumbnailURI,
            templatesMetadataItem.isPremium
        ) // TODO db saves relative URI (just as appears in JSON)
    }

    private fun saveItemsToDB(items: MutableList<FeedItemEntity>): Completable {
        val c: Completable = feedDatabase.FeedItemDao().insertAll(items)
        return c
    }
}
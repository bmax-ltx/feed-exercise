package com.lightricks.feedexercise.ui.feed

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.lightricks.feedexercise.data.FeedItem
import com.lightricks.feedexercise.network.FeedApi
import com.lightricks.feedexercise.network.GetFeedResponse
import com.lightricks.feedexercise.network.TemplatesMetadataItem
import com.lightricks.feedexercise.util.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Error
import java.lang.IllegalArgumentException

/**
 * This view model manages the data for [FeedFragment].
 */
open class FeedViewModel : ViewModel() {
    private val isLoading = MutableLiveData<Boolean>()
    private val isEmpty = MutableLiveData<Boolean>()
    private val feedItems = MediatorLiveData<List<FeedItem>>()
    private val networkErrorEvent = MutableLiveData<Event<String>>()

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getIsEmpty(): LiveData<Boolean> = isEmpty
    fun getFeedItems(): LiveData<List<FeedItem>> = feedItems
    fun getNetworkErrorEvent(): LiveData<Event<String>> = networkErrorEvent

    init {
        refresh()
    }

    fun refresh() {
        //todo: fix the implementation
        isLoading.value = false
        isEmpty.value = true
        loadFeedItems()
    }

    @SuppressLint("CheckResult")
    fun loadFeedItems() {
        FeedApi.service.getFeed()
            .subscribeOn(Schedulers.io()) //[1]
            .observeOn(AndroidSchedulers.mainThread()) //[2]
            .subscribe({ feedResponse ->
                handleResponse(feedResponse)
            },{ error ->
                handleNetworkError(error)
            })
    }

    private fun handleResponse(feedResponse: GetFeedResponse){
        val output: MutableList<FeedItem> = emptyList<FeedItem>().toMutableList()
        for (item in feedResponse.templatesMetadata){
            output.add(templatesMetadataToFeedItem(item))
        }
        feedItems.value = output
    }

    private fun templatesMetadataToFeedItem(templatesMetadataItem: TemplatesMetadataItem): FeedItem{
        return FeedItem(templatesMetadataItem.id, "https://assets.swishvideoapp.com/Android/demo/catalog/thumbnails/"+templatesMetadataItem.templateThumbnailURI, templatesMetadataItem.isPremium)
    }

    private fun handleNetworkError(error: Throwable) { // TODO right error?
        Log.d("FeedModelView", "network error") // TODO handle
    }
}

/**
 * This class creates instances of [FeedViewModel].
 * It's not necessary to use this factory at this stage. But if we will need to inject
 * dependencies into [FeedViewModel] in the future, then this is the place to do it.
 */
class FeedViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            throw IllegalArgumentException("factory used with a wrong class")
        }
        @Suppress("UNCHECKED_CAST")
        return FeedViewModel() as T
    }
}
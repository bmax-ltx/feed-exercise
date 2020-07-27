package com.lightricks.feedexercise.data

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import com.lightricks.feedexercise.database.Entity
import com.lightricks.feedexercise.database.FeedDao
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.FeedResponse
import com.lightricks.feedexercise.ui.feed.FeedViewModel
import com.lightricks.feedexercise.ui.feed.FeedViewModelFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * This is our data layer abstraction. Users of this class don't need to know
 * where the data actually comes from (network, database or somewhere else).
 */
class FeedRepository(private val feedDao: FeedDao, private val feedApiService: FeedApiService) {

    private var compositeDisposable: CompositeDisposable? = null
    private var feedItemsLiveData: MutableLiveData<List<FeedItem>> = MutableLiveData()
    fun refresh(): Completable{
        compositeDisposable = CompositeDisposable()
        Completable.create()
        compositeDisposable?.add(
            feedApiService.getMetadataList()
                .subscribeOn(Schedulers.io()) //[1]
                .observeOn(AndroidSchedulers.mainThread()) //[2]
                .subscribe({ feedResponse ->
                    handleResponse(feedResponse)
                }, { error ->
                    handleNetworkError(error)
                })
        )
        return feedItemsLiveData
    }

    @SuppressLint("CheckResult")
    fun handleResponse(json: FeedResponse) {
        val list = jsonToEntity(json)
        feedDao.insertList(list)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                Log.i("response", "database size " + feedDao.getSize())
                pushToUI()
            })

            { throwable ->
                run {
                    Log.e("Complete", throwable.message)
                }
            }
        Log.i("network response", "result ")
    }

    fun pushToUI(){
        val dbList = feedDao.getAllItems()
        val uiList = ArrayList<FeedItem>()
        for(db in dbList.value!!){
            uiList.add(FeedItem(db.id, db.thumbnailUrl!!, db.isPremium!!))
        }
        feedItemsLiveData.postValue(uiList)
    }

    fun handleNetworkError(error: Throwable) {
        Log.e("network error", error.message)
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
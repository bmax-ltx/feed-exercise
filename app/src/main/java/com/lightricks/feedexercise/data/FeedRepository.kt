package com.lightricks.feedexercise.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import com.lightricks.feedexercise.database.Entity
import com.lightricks.feedexercise.database.FeedDao
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.ui.feed.FeedViewModel
import com.lightricks.feedexercise.ui.feed.FeedViewModelFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
class FeedRepository(private val feedDao: FeedDao) {
//    val database = FeedDatabase.getExistingDatabase()
//    val allContents = database.feedDao().getAllItems()
//    val feedItems: LiveData<List<FeedItem>> = Transformations.map(allContents) {//[1]
//        it.toFeedItems()//[2] }
//    }
//
//    fun List<Entity>.toFeedItems(): List<FeedItem> {
//        return map {
//            FeedItem(it.id, it.thumbnailUrl!!, it.isPremium!!)
//        }
//    }


}
package com.lightricks.feedexercise.network

import androidx.lifecycle.LiveData
import com.lightricks.feedexercise.data.FeedItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


interface FeedApiService {
    @GET("Android/demo/feed.json" ) // TODO constant
    fun getFeed() : Single<GetFeedResponse>
}

object FeedApi {
    private val moshi : Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl("https://assets.swishvideoapp.com/") // TODO last / needed?
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    val service: FeedApiService = retrofit.create(FeedApiService::class.java)
}
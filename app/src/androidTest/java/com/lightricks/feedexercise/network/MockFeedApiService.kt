package com.lightricks.feedexercise.network

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Single
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

/**
 * todo: implement the mock feed API service here
 */

class MockFeedApiService(context: Context) : FeedApiService {

    lateinit var feedResponse: FeedResponse

    fun initFeedResponse(context: Context) {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter = moshi.adapter(FeedResponse::class.java)
        val str = loadJSONFromAsset(context)
        feedResponse = adapter.fromJson(str!!)!!
    }

    private fun loadJSONFromAsset(context: Context): String? {
        val json = try {
            val `is`: InputStream = context.assets.open("get_feed_response.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }


    override fun getFeed(): Single<FeedResponse> {
        return Single.just(feedResponse)
//        return Single.just(getHardcodedFeedResponse())
    }

    init {
        initFeedResponse(context)
    }
}

package com.lightricks.feedexercise.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lightricks.feedexercise.R
import com.lightricks.feedexercise.database.Entity
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.FeedResponse
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
 * This is the main entry point into the app. This activity shows the [FeedFragment].
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

    private var compositeDisposable: CompositeDisposable? = null

    private fun loadData() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val requestInterface = Retrofit.Builder()
            .baseUrl("https://assets.swishvideoapp.com/Android/demo/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(FeedApiService::class.java)

        compositeDisposable = CompositeDisposable()
        compositeDisposable?.add(requestInterface.getMetadataList()
            .subscribeOn(Schedulers.io()) //[1]
            .observeOn(AndroidSchedulers.mainThread()) //[2]
            .subscribe({ feedResponse ->
                handleResponse(feedResponse)
            }, { error ->
                handleNetworkError(error)
            })
        )


    }

    @SuppressLint("CheckResult")
    fun handleResponse(json: FeedResponse) {
        val database = FeedDatabase.getDatabase(this)
        database.feedDao().insertList(jsonToEntity(json))
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.i("complete", "success")})
            { throwable -> Log.e("Complete", throwable.message)}
        Log.i("network response", "result ")
    }

    fun handleNetworkError(error: Throwable) {
        Log.e("network error", error.message)
    }

    private fun jsonToEntity(json: FeedResponse): List<Entity>{
        val ls = ArrayList<Entity>()
        for (j in json.templatesMetadata){
            val ent = Entity(j.id, j.templateThumbnailURI, j.isPremium)
            ls.add(ent)
        }
        return ls
    }

}
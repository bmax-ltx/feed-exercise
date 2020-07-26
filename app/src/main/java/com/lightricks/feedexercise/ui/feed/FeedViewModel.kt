package com.lightricks.feedexercise.ui.feed

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.lightricks.feedexercise.data.FeedItem
import com.lightricks.feedexercise.database.Entity
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.network.FeedResponse
import com.lightricks.feedexercise.util.Event
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.IllegalArgumentException

/**
 * This view model manages the data for [FeedFragment].
 */
open class FeedViewModel(application: Application) : AndroidViewModel(application) {
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
        isLoading.value = true
        isEmpty.value = true
        loadData()
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
        compositeDisposable?.add(
            requestInterface.getMetadataList()
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
        val database = FeedDatabase.getDatabase(getApplication())
        database.feedDao().insertList(jsonToEntity(json))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                isLoading.postValue(false)
                isEmpty.postValue(FeedDatabase.getDatabase(getApplication()).feedDao().getSize() <= 0)
                Log.i("response", "database size " + FeedDatabase.getDatabase(getApplication()).feedDao().getSize())
            })

            { throwable ->
                run {
                    isLoading.postValue(true)
                    Log.e("Complete", throwable.message)
                }
            }
        Log.i("network response", "result ")
    }

    fun handleNetworkError(error: Throwable) {
        Log.e("network error", error.message)
    }

    private fun jsonToEntity(json: FeedResponse): List<Entity> {
        val ls = ArrayList<Entity>()
        for (j in json.templatesMetadata) {
            val ent = Entity(j.id, j.templateThumbnailURI, j.isPremium)
            ls.add(ent)
        }
        return ls
    }

}

/**
 * This class creates instances of [FeedViewModel].
 * It's not necessary to use this factory at this stage. But if we will need to inject
 * dependencies into [FeedViewModel] in the future, then this is the place to do it.
 */
class FeedViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            throw IllegalArgumentException("factory used with a wrong class")
        }
        @Suppress("UNCHECKED_CAST")
        return FeedViewModel(app) as T
    }
}
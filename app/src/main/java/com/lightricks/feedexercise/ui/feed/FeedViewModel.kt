package com.lightricks.feedexercise.ui.feed

import android.app.Application
import androidx.lifecycle.*
import com.lightricks.feedexercise.data.FeedItem
import com.lightricks.feedexercise.data.FeedRepository
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.network.FeedApiService
import com.lightricks.feedexercise.util.Event
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.IllegalArgumentException

/**
 * This view model manages the data for [FeedFragment].
 */
open class FeedViewModel(application: Application, val feedRepository: FeedRepository) : AndroidViewModel(application) {
    private val isLoading = MutableLiveData<Boolean>()
    private val isEmpty: MediatorLiveData<Boolean>

    //    private val feedItems = MediatorLiveData<List<FeedItem>>()
    private val networkErrorEvent = MutableLiveData<Event<String>>()
//    private var feedRepository: FeedRepository
    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getIsEmpty(): LiveData<Boolean> = isEmpty
    fun getFeedItems(): LiveData<List<FeedItem>> = feedRepository.getLiveData()
    fun getNetworkErrorEvent(): LiveData<Event<String>> = networkErrorEvent

    init {
        isEmpty = MediatorLiveData<Boolean>().apply {
            this.addSource(getFeedItems()) {
                this.postValue(it.isEmpty())
            }
        }
    }

    fun refresh() {
        isLoading.postValue(true)
        val completable = feedRepository.refresh()
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { isLoading.postValue(false) },
                { networkErrorEvent.postValue(Event<String>(it?.message ?: "error"))
                isLoading.postValue(false)}
            )
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
        val feedApiService = initRetrofit()
        val feedDao = FeedDatabase.getDatabase(app).feedDao()
        val feedRepository = FeedRepository(feedDao, feedApiService)
        @Suppress("UNCHECKED_CAST")
        return FeedViewModel(app, feedRepository) as T
    }


    private fun initRetrofit(): FeedApiService {
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
        return requestInterface
    }
}
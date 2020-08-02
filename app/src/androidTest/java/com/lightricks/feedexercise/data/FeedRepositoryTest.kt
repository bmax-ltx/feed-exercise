package com.lightricks.feedexercise.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lightricks.feedexercise.database.FeedDao
import com.lightricks.feedexercise.database.FeedDatabase
import com.lightricks.feedexercise.network.MockFeedApiService
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class FeedRepositoryTest {

    //execute each task synchronously using Architecture Components
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var feedRepository: FeedRepository
    private lateinit var feedDao: FeedDao
    private lateinit var db: FeedDatabase
    private val testSetSize = 10
    @Before
    fun setup() {
        val mockFeedApiService =
            MockFeedApiService(ApplicationProvider.getApplicationContext<Context>())
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>()
            , FeedDatabase::class.java
        ).build()

        feedDao = db.feedDao()
        feedRepository = FeedRepository(feedDao, mockFeedApiService)
    }

    @Test
    fun testSize() {
        val observe = feedRepository.refresh().test()
        observe.awaitTerminalEvent()
        observe.assertComplete()
        assertTrue(feedDao.getSize() == testSetSize)
    }

    @Test
    fun testName() {
        val observe = feedRepository.refresh().test()
        observe.awaitTerminalEvent()
        observe.assertComplete()
        val res = feedDao.getAllItems().blockingObserve()
        assertTrue(res!![0].id == "01E18PGE1RYB3R9YF9HRXQ0ZSD")
    }


    @Test
    fun testFeedRepository(){
        val observe = feedRepository.refresh().test()
        observe.awaitTerminalEvent()
        observe.assertComplete()
        val feedItems = feedRepository.getLiveData().blockingObserve()
        assertTrue(feedItems!!.size == testSetSize)
    }


    @After
    fun cleanup() {
        db.close()
    }

}

private fun <T> LiveData<T>.blockingObserve(): T? {
    var value: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(t: T) {
            value = t
            latch.countDown()
            removeObserver(this)
        }
    }

    observeForever(observer)
    latch.await(5, TimeUnit.SECONDS)
    return value
}

package com.lightricks.feedexercise.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.lightricks.feedexercise.R


/**
 * This is the main entry point into the app. This activity shows the [FeedFragment].
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
//        @Suppress("DEPRECATION")
//        ViewModelProviders.of(this, FeedViewModelFactory()).get(FeedViewModel::class.java)

    }

    override fun onDestroy() {
        super.onDestroy()
//        compositeDisposable?.clear()
    }


}
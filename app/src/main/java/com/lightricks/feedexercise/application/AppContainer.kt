package com.lightricks.feedexercise.application

import android.content.Context
import com.lightricks.feedexercise.database.FeedDatabase

class AppContainer(context: Context) {

    val database = FeedDatabase.getDatabase(context)

}
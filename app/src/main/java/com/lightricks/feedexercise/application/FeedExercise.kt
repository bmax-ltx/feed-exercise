package com.lightricks.feedexercise.application

import android.app.Application

class FeedExercise : Application() {
    val appContainer = AppContainer(this)
}
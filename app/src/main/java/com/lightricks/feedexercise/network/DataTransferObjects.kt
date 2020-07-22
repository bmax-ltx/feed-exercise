package com.lightricks.feedexercise.network

import android.os.Parcel
import android.os.Parcelable
import com.lightricks.feedexercise.data.FeedItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

/**
 * todo: add Data Transfer Object data class(es) here
 */




data class FeedResponse(
    val templatesMetadata: List<TemplatesMetadataItem>
)

data class TemplatesMetadataItem(
    val configuration: String?,
    val id: String?,
    val isNew: Boolean,
    val isPremium: Boolean,
    val templateName: String?,
    val templateThumbnailURI: String?
)

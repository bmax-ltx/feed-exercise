package com.lightricks.feedexercise.network

/**
 * todo: add Data Transfer Object data class(es) here
 */




data class FeedResponse(
    val templatesMetadata: List<MetadataItem>
)

data class MetadataItem(
    val configuration: String,
    val id: String,
    val isNew: Boolean,
    val isPremium: Boolean,
    val templateName: String,
    val templateThumbnailURI: String
)

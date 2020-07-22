package com.lightricks.feedexercise.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * todo: add Room's Entity data class(es) here
 */
@Entity(tableName = "feed_table")
data class ItemEntity(
    @PrimaryKey
    val id: String, // Kotlin note: "val" means read-only value.
    val thumbnail_url: String,
    val is_premium: Boolean)
package com.lightricks.feedexercise.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * todo: add Room's Entity data class(es) here
 */
@Entity(tableName = "feed_table")
data class Entity(
    @PrimaryKey
    val id: String, // Kotlin note: "val" means read-only value.
    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @ColumnInfo(name = "is_premium")
    val isPremium: Boolean?)
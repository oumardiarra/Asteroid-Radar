package com.udacity.asteroidradar.database

import androidx.lifecycle.Transformations
import androidx.lifecycle.Transformations.map
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.PictureOfDay

@Entity
class PictureOfDayEntity constructor(
    @PrimaryKey
    val url: String,
    val mediaType: String, val title: String

)

fun PictureOfDayEntity.asDomainModel(): PictureOfDay {

    return PictureOfDay(
        url = this.url,
        mediaType = this.mediaType,
        title = this.title
    )

}
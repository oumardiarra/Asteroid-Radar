package com.udacity.asteroidradar.network

import androidx.lifecycle.Transformations.map
import com.squareup.moshi.Json
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.PictureOfDayEntity

data class NetworkAsteroid(
    val id: Long, val codename: String, val closeApproachDate: String,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)


data class NetworkPictureOfDay(
    @Json(name = "media_type") val mediaType: String, val url: String,
    val title: String
)

fun NetworkPictureOfDay.asDatabasModel(): PictureOfDayEntity {

    return PictureOfDayEntity(
        url = this.url,
        mediaType = this.mediaType,
        title = this.title
    )
}

fun List<NetworkAsteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}
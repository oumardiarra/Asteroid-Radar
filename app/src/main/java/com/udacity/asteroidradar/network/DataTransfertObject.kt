package com.udacity.asteroidradar.network

import androidx.lifecycle.Transformations.map
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.DatabaseAsteroid

data class NetworkAsteroid(val id: Long, val codename: String, val closeApproachDate: String,
                           val absoluteMagnitude: Double, val estimatedDiameter: Double,
                           val relativeVelocity: Double, val distanceFromEarth: Double,
                           val isPotentiallyHazardous: Boolean)




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
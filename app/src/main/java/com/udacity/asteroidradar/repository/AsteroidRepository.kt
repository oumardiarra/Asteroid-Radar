package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.getFormattedDate
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.asDatabaseModel
import org.json.JSONObject
import timber.log.Timber
import java.lang.Exception
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.main.AsteroidFilters
import com.udacity.asteroidradar.network.asDatabasModel


class AsteroidRepository(private val database: AsteroidDatabase) {

    fun getAsteroid(asteroidFilters: AsteroidFilters): LiveData<List<Asteroid>> {
        Timber.i("asteroidFilters ${asteroidFilters}")
        return when (asteroidFilters) {
            AsteroidFilters.WEEK -> Transformations.map(database.asteroidDao.getWeekAsteroid()) {
                it.asDomainModel()

            }
            AsteroidFilters.ALL -> Transformations.map(database.asteroidDao.getSavedAsteroid()) {
                it.asDomainModel()

            }
            AsteroidFilters.TODAY -> Transformations.map(database.asteroidDao.getTodayAsteroid()) {
                it.asDomainModel()

            }
            else -> Transformations.map(database.asteroidDao.getAsteroids()) {
                it.asDomainModel()

            }
        }

    }

    val pictureOfDay = Transformations.map(database.asteroidDao.getPictureOfDAy()) {
        it?.asDomainModel()
    }

    suspend fun refreshAsteroid() {
        withContext(Dispatchers.IO) {
            try {
                val formattedDateList = getFormattedDate()

                Timber.i("Start getting Json from the Service: ")
                val jsonAsteroid = Network.retrofitService.getAsteroidList(
                    formattedDateList.get(0), formattedDateList.get(1),
                    BuildConfig.NASA_API_KEY
                )
                Timber.i("End getting Json from the Service: ")
                Timber.i("Formating string result to Json: ")
                val jsonObject = JSONObject(jsonAsteroid)
                Timber.i("End Formating string result to Json: ")
                Timber.i("Start parseAsteroidsJsonResult: ")
                val listNetworkAsteroid = parseAsteroidsJsonResult(jsonObject)
                Timber.i("End parseAsteroidsJsonResult ")
                database.asteroidDao.insertAll(*listNetworkAsteroid.asDatabaseModel())

            } catch (e: Exception) {
                Timber.e("Unable to RefreshAsteroide: " + e.message)

            }

        }
    }

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Getting Picture of day")
                val pictureOfDay =
                    Network.imageOfDayService.getAsteroidOfDay(BuildConfig.NASA_API_KEY)
                Timber.i("Picture of day is ${pictureOfDay}")
                database.asteroidDao.insertPictureOfDay(pictureOfDay.asDatabasModel())
                Timber.i("End Getting Picture of day")
            } catch (exc: Exception) {
                Timber.e("Unable to refreshPictureOfDay: " + exc.message)
            }
        }
    }


}
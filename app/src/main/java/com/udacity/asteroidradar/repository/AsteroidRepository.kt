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
import com.udacity.asteroidradar.database.asDomainModel



class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroid() {
        withContext(Dispatchers.IO) {
            try {
                val formattedDateList = getFormattedDate()

                Timber.i("Start getting Json from the Service: ")
                val jsonAsteroid = Network.retrofitService.getAsteroidList(
                    formattedDateList.get(0), formattedDateList.get(1),
                    Constants.API_KEY
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
}
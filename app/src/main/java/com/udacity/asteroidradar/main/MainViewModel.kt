package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

enum class AsteroidFilters { DEFAULT, WEEK, TODAY, ALL }
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var _asteroidFilter = MutableLiveData<AsteroidFilters>()
    val asteroidFilters: LiveData<AsteroidFilters>
        get() = _asteroidFilter
    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)
    val asteroids = Transformations.switchMap(asteroidFilters) {
        when (it!!) {

            AsteroidFilters.ALL -> repository.getAsteroid(AsteroidFilters.ALL)
            AsteroidFilters.WEEK -> repository.getAsteroid(AsteroidFilters.WEEK)
            AsteroidFilters.TODAY -> repository.getAsteroid(AsteroidFilters.TODAY)
            else -> repository.getAsteroid(AsteroidFilters.DEFAULT)
        }

    }
    // val asteroids = repository.getAsteroid(AsteroidFilters.DEFAULT)

    //  val asteroids = repository.asteroids
    val picOfDay = repository.picOfDay
    private var _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    init {
        _asteroidFilter.value = AsteroidFilters.DEFAULT
        refreshAsteroid()
        getPicOfDay()
    }

    private fun refreshAsteroid() {
        viewModelScope.launch {

            repository.refreshAsteroid()
        }
    }

    private fun getPicOfDay() {
        viewModelScope.launch {
            repository.getPictureOfDay()
        }
    }

    fun updateListAsteroid(asteroidFilters: AsteroidFilters) {
        _asteroidFilter.value = asteroidFilters
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    /*
    *
    * Factory to construct MainViewModel with parameter
    *
    * */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}
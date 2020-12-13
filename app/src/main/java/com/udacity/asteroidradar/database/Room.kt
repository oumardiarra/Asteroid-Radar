package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("select * from DatabaseAsteroid where date(closeApproachDate)>=date('now') order by date(closeApproachDate) desc ")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("select * from databaseasteroid order by date(closeApproachDate) desc")
    fun getSavedAsteroid(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid  where date(closeApproachDate)=date('now')")
    fun getTodayAsteroid(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid  where date(closeApproachDate) between date('now') and date('now','+7 days') order by date(closeApproachDate) desc")
    fun getWeekAsteroid(): LiveData<List<DatabaseAsteroid>>
}


@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "Asteroids"
            ).build()
        }
    }
    return INSTANCE
}

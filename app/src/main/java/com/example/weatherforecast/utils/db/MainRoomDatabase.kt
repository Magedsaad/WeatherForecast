package com.example.weatherforecast.utils.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weatherforecast.features.home.data.converters.WeatherListConverter
import com.example.weatherforecast.features.home.data.local.CitiesDao
import com.example.weatherforecast.features.home.data.local.WeathersDao
import com.example.weatherforecast.features.home.data.models.City
import com.example.weatherforecast.features.home.data.models.WeatherData
import com.example.weatherforecast.utils.AppUtils
import com.example.weatherforecast.utils.Const
import com.example.weatherforecast.utils.call

@Database(entities = [City::class, WeatherData::class], version = 1, exportSchema = false)
@TypeConverters(
  WeatherListConverter::class
)
abstract class MainRoomDatabase : RoomDatabase() {
  abstract fun citiesDao(): CitiesDao
  abstract fun weathersDao(): WeathersDao
}


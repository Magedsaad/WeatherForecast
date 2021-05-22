package com.example.weatherforecast

import android.app.Application
import com.example.weatherforecast.di.AppComponent
import com.example.weatherforecast.di.ContextModule
import com.example.weatherforecast.di.DaggerAppComponent

class App : Application() {
  lateinit var appComponent: AppComponent
    private set

  override fun onCreate() {
    super.onCreate()
    appComponent = DaggerAppComponent.factory()
      .create(ContextModule(this))
  }
}
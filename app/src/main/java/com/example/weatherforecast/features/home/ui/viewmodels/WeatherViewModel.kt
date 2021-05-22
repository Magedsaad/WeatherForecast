package com.example.weatherforecast.features.home.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.features.home.data.models.City
import com.example.weatherforecast.features.home.data.models.WeatherData
import com.example.weatherforecast.features.home.domain.GetCityWeatherByIdUseCase
import com.example.weatherforecast.features.home.domain.GetFavoriteCitiesUseCase
import com.example.weatherforecast.features.home.domain.PopulateCitiesUseCase
import com.example.weatherforecast.features.home.domain.SearchForCityUseCase
import com.example.weatherforecast.features.home.domain.SetDefaultCityUseCase
import com.example.weatherforecast.features.home.domain.UpdateCityUseCase
import com.example.weatherforecast.utils.Const
import com.example.weatherforecast.utils.SessionManager
import com.example.weatherforecast.utils.asMappedResourceLiveData
import com.example.weatherforecast.utils.asResourceLiveData
import com.example.weatherforecast.utils.mappedLiveData
import com.example.weatherforecast.utils.result.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
  private val populateCitiesUseCase: PopulateCitiesUseCase,
  private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
  private val updateCityUseCase: UpdateCityUseCase,
  private val searchForCityUseCase: SearchForCityUseCase,
  private val setDefaultCityUseCase: SetDefaultCityUseCase,
  private val getCityWeatherByIdUseCase: GetCityWeatherByIdUseCase,
  private val sessionManager: SessionManager
) : ViewModel() {

  fun populateCitiesFistTime(): LiveData<Resource<Unit>> =
    mappedLiveData("populateCitiesFistTime") {
      if (sessionManager.shouldPopulateCities) {
        populateCitiesUseCase.execute()
        sessionManager.shouldPopulateCities = false
      }
    }

  fun getFavoriteCities(): LiveData<Resource<List<City>>> =
    getFavoriteCitiesUseCase.get().asResourceLiveData("getFavoriteCities")

  fun updateCity(city: City) {
    viewModelScope.launch(Dispatchers.IO) {
      updateCityUseCase.execute(city)
    }
  }

  fun searchForCity(query: String): LiveData<Resource<List<City>>> =
    searchForCityUseCase.get(query).asResourceLiveData("searchForCity")

  fun setDefaultCity(cityName: String?, countryCode: String?): LiveData<Resource<Unit>> =
    mappedLiveData("setDefaultCity") {
      val affectedRows = setDefaultCityUseCase.execute(cityName, countryCode)
      if (affectedRows != 1) {
        setDefaultCityUseCase.execute(Const.LONDON, Const.UK)
      }
    }

  @ExperimentalCoroutinesApi
  fun getCityWeatherForecast(cityId: Int): LiveData<Resource<List<WeatherData>>> =
    getCityWeatherByIdUseCase.get(cityId).asMappedResourceLiveData("getCityWeatherForecast")
}
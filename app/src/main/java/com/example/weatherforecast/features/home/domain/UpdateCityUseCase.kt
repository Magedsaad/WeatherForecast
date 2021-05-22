package com.example.weatherforecast.features.home.domain

import com.example.weatherforecast.features.home.data.models.City
import javax.inject.Inject

class UpdateCityUseCase @Inject constructor(private val citiesRepository: CitiesRepository) {
  suspend fun execute(city: City) = citiesRepository.updateCity(city)
}
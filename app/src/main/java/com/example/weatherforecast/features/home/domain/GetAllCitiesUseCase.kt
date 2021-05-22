package com.example.weatherforecast.features.home.domain

import com.example.weatherforecast.features.home.data.models.City
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCitiesUseCase @Inject constructor(private val citiesRepository: CitiesRepository) {

  fun get(): Flow<List<City>> = citiesRepository.getAllCities()
}
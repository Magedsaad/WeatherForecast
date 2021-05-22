package com.example.weatherforecast.features.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.features.home.data.models.City
import com.example.weatherforecast.features.home.ui.adapters.CitiesAdapter
import com.example.weatherforecast.features.home.ui.adapters.CitiesAdapter.CityActionsListener
import com.example.weatherforecast.features.home.ui.viewmodels.WeatherViewModel
import com.example.weatherforecast.utils.appComponent
import com.example.weatherforecast.utils.loge
import com.example.weatherforecast.utils.result.Resource
import com.example.weatherforecast.utils.result.ResourceType
import com.example.weatherforecast.utils.showErrorMessage
import com.example.weatherforecast.utils.showLoading
import com.example.weatherforecast.utils.viewmodel.ViewModelFactory
import com.example.weatherforecast.utils.views.BaseFragment
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>(), CityActionsListener {
  private lateinit var searchView: SearchView
  @Inject lateinit var viewModelFactory: ViewModelFactory
  private val weatherViewModel: WeatherViewModel by activityViewModels { viewModelFactory }
  private val citiesAdapter: CitiesAdapter by lazy { CitiesAdapter(this) }

  override fun onBind(
    inflater: LayoutInflater,
    container: ViewGroup?
  ): FragmentHomeBinding {
    setHasOptionsMenu(true)
    appComponent.inject(this)
    return FragmentHomeBinding.inflate(inflater, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupFavoriteCitiesRecyclerView()
    setObservers()
  }

  private fun setupFavoriteCitiesRecyclerView() {
    binding.rvSearchCity.layoutManager = LinearLayoutManager(context)
    binding.rvSearchCity.adapter = citiesAdapter
  }

  private fun setObservers() {
    weatherViewModel.getFavoriteCities()
      .observe(viewLifecycleOwner, this::handleFavoriteCitiesResult)
  }

  private fun handleFavoriteCitiesResult(result: Resource<List<City>>) {
    when (result.resourceType) {
      ResourceType.LOADING -> setLoading(true)
      ResourceType.SUCCESS -> {
        setLoading(false)
        submitFavoriteCityList(result.data)
      }
      ResourceType.EMPTY_DATA -> setError(getString(R.string.error_no_favorite_cities_found))
      else -> setError(getString(R.string.connection_error))
    }
  }

  private fun setLoading(isLoading: Boolean) = binding.layoutProgress.showLoading(isLoading)

  private fun setError(msg: String) = binding.layoutProgress.showErrorMessage(msg)

  private fun submitFavoriteCityList(list: List<City>?) = citiesAdapter.submitList(list)

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_main, menu)
    val searchItem: MenuItem = menu.findItem(R.id.action_search)
    searchView = searchItem.actionView as SearchView
    searchView.isEnabled = false
    searchView.setOnSearchClickListener {
      loge("setOnSearchClickListener called")
      SearchCityFragment.show(navController)
      searchView.onActionViewCollapsed()
    }
    return super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onCityClick(city: City) {
    CityWeatherFragment.show(navController, city)
  }

  override fun onUpdateCityFavorite(city: City) {
    weatherViewModel.updateCity(city)
  }
}

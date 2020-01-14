package com.jojartbence.archeologicalfieldwork

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jojartbence.helpers.showImageUsingGlide
import com.jojartbence.model.SiteModel
import kotlinx.android.synthetic.main.map_of_all_sites_fragment.*


class MapOfAllSitesFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private val viewModel by lazy { ViewModelProviders.of(this)[MapOfAllSitesViewModel::class.java] }

    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_of_all_sites_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)


        val selectedSiteObserver = Observer<SiteModel> {
            showSelectedSite()
        }
        viewModel.selectedSite.observe(this, selectedSiteObserver)


        allSitesMapView.onCreate(savedInstanceState)
        createMap()
    }


    override fun onMarkerClick(marker: Marker): Boolean {
        viewModel.selectSiteById(marker.tag as String)
        return true
    }


    private fun createMap() {
        allSitesMapView.getMapAsync {
            val map = it
            map.setOnMarkerClickListener(this)
            map.uiSettings.isZoomControlsEnabled = true
            map.setMaxZoomPreference(17.0f)
            if (viewModel.getAllSites().isNotEmpty()) {

                val cameraBuilder = LatLngBounds.builder()
                viewModel.getAllSites().forEach {
                    val loc = LatLng(it.location.lat, it.location.lng)
                    val options = MarkerOptions().title(it.title).position(loc)
                    map.addMarker(options).tag = it.id
                    cameraBuilder.include(loc)
                }
                val bounds = cameraBuilder.build()

                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30))

                viewModel.selectedSite.value =
                    viewModel.selectedSite.value ?: viewModel.getAllSites().last()
            }
        }
    }


    private fun showSelectedSite() {
        selectedSiteTitle.text = viewModel.selectedSite.value?.title
        selectedSiteDescription.text = viewModel.selectedSite.value?.description
        showImageUsingGlide(activity!!.applicationContext, viewModel.selectedSite.value?.imageContainerList?.get(0), selectedSiteImage)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        allSitesMapView.onDestroy()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        allSitesMapView.onLowMemory()
    }


    override fun onPause() {
        super.onPause()
        allSitesMapView.onPause()
    }


    override fun onResume() {
        super.onResume()
        allSitesMapView.onResume()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        allSitesMapView.onSaveInstanceState(outState)
    }


    override fun onStart() {
        super.onStart()
        allSitesMapView.onStart()
    }


    override fun onStop() {
        super.onStop()
        allSitesMapView.onStop()
    }


}

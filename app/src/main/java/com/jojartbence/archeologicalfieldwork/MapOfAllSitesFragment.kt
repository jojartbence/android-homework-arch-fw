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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jojartbence.model.SiteModel
import kotlinx.android.synthetic.main.fragment_site.*
import kotlinx.android.synthetic.main.fragment_site.siteTitle
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
        viewModel.selectSiteById(marker.tag as Long)
        return true
    }


    private fun createMap() {
        allSitesMapView.getMapAsync {
            val map = it
            map.setOnMarkerClickListener(this)
            map.uiSettings.isZoomControlsEnabled = true
            viewModel.getAllSites().forEach {
                val loc = LatLng(it.location.lat, it.location.lng)
                val options = MarkerOptions().title(it.title).position(loc)
                map.addMarker(options).tag = it.id
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
                viewModel.selectedSite.value = it
            }
        }
    }


    private fun showSelectedSite() {
        selectedSiteTitle.text = viewModel.selectedSite.value?.title
        selectedSiteDescription.text = viewModel.selectedSite.value?.description
        selectedSiteImage.setImageBitmap(viewModel.getImageOfSelectedSite(activity!!.applicationContext))
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

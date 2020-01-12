package com.jojartbence.archeologicalfieldwork

import android.app.Activity
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
import com.jojartbence.helpers.bitmapDescriptorFromVector
import com.jojartbence.model.Location
import kotlinx.android.synthetic.main.site_navigator_fragment.*


class SiteNavigatorFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this)[SiteNavigatorViewModel::class.java] }

    lateinit var navController: NavController

    var googleMap: GoogleMap? = null
    var liveLocationMarker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        val liveLocationObserver = Observer<LatLng> {
            refreshLiveLocationMarker(it)
        }
        viewModel.liveLocation.observe(this, liveLocationObserver)

        viewModel.attachTitle(arguments?.getString("siteTitle") ?: getString(R.string.navigator_site_default_title))
        viewModel.attachLocation(arguments?.getParcelable("location"))
        viewModel.initLocationService(activity as Activity)


        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.site_navigator_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            it.uiSettings.isZoomControlsEnabled = true

            val markerOptions = MarkerOptions()
            markerOptions.title(viewModel.siteTitle)
            markerOptions.draggable(false)
            markerOptions.position(viewModel.siteLocation.getLatLng())
            it.addMarker(markerOptions)
            siteLat.text = "Lat: %.6f".format(viewModel.siteLocation.lat)
            siteLng.text = "Lat: %.6f".format(viewModel.siteLocation.lng)

            it.moveCamera(CameraUpdateFactory.newLatLngZoom(viewModel.siteLocation.getLatLng(), viewModel.siteLocation.zoom))
            it.isMyLocationEnabled = true
            googleMap = it
        }
    }


    private fun refreshLiveLocationMarker(position: LatLng) {
        liveLocationMarker?.remove()

        val markerOptions = MarkerOptions()
        markerOptions.title(getString(R.string.navigator_your_location))
        markerOptions.draggable(false)
        markerOptions.icon(bitmapDescriptorFromVector(activity!!.applicationContext, R.drawable.ic_marker_live_location))
        markerOptions.position(position)

        liveLocationMarker = googleMap?.addMarker(markerOptions)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(position))

        yourLat.text = "Lat: %.6f".format(position.latitude)
        yourLng.text = "Lng: %.6f".format(position.longitude)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


    override fun onPause() {
        super.onPause()
        mapView.onPause()
        viewModel.stopLocationUpdates()
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
        viewModel.startLocationUpdates()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }


    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }



}

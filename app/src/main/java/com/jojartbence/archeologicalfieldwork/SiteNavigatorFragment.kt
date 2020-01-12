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

        val liveLocationObserver = Observer<Location> {
            refreshLiveLocationMarker(it)
        }
        viewModel.liveLocation.observe(this, liveLocationObserver)


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
            it.isMyLocationEnabled = true
            googleMap = it
        }
    }


    private fun refreshLiveLocationMarker(position: Location) {
        liveLocationMarker?.remove()

        val markerOptions = MarkerOptions()
        markerOptions.title("Your location")
        markerOptions.draggable(false)
        markerOptions.icon(bitmapDescriptorFromVector(activity!!.applicationContext, R.drawable.ic_marker_live_location))
        markerOptions.position(position.getLatLng())

        liveLocationMarker = googleMap?.addMarker(markerOptions)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position.getLatLng(), position.zoom))

        lat.text = "Lat: %.6f".format(position.lat)
        lng.text = "Lng: %.6f".format(position.lng)
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

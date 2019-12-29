package com.jojartbence.archeologicalfieldwork

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jojartbence.model.Location
import kotlinx.android.synthetic.main.fragment_site.*
import kotlinx.android.synthetic.main.fragment_site.mapView
import kotlinx.android.synthetic.main.site_edit_location_fragment.*


class SiteEditLocationFragment : Fragment(), GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    private val viewModel by lazy { ViewModelProviders.of(this)[SiteEditLocationViewModel::class.java] }
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.attachLocation(arguments?.getParcelable("location"))

        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.site_edit_location_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            it.setOnMarkerDragListener(this)
            it.setOnMarkerClickListener(this)
            it?.uiSettings?.isZoomControlsEnabled = true
            val options = MarkerOptions()
                .title("Placemark")
                .snippet("GPS : " + viewModel.getLatLng().toString())
                .draggable(true)
                .position(viewModel.getLatLng())
            it.addMarker(options)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(viewModel.getLatLng(), viewModel.location.zoom))
            lat.text = "Lat: %.6f".format(viewModel.location.lat)
            lng.text = "Lng: %.6f".format(viewModel.location.lng)
        }
    }


    override fun onMarkerDragEnd(marker: Marker) {
        viewModel.location.lat = marker.position.latitude
        viewModel.location.lng = marker.position.longitude
    }


    override fun onMarkerDragStart(marker: Marker) {
    }


    override fun onMarkerDrag(marker: Marker) {
        lat.text = "Lat: %.6f".format(marker.position.latitude)
        lng.text = "Lng: %.6f".format(marker.position.longitude)
    }


    override fun onMarkerClick(marker: Marker): Boolean {
        marker.snippet = "GPS : " + viewModel.getLatLng().toString()
        return false
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
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
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

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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jojartbence.model.Location
import kotlinx.android.synthetic.main.fragment_site.*


class SiteEditLocationFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this)[SiteEditLocationViewModel::class.java] }
    lateinit var navController: NavController

    lateinit var location: Location
    val defaultLocation = Location()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.site_edit_location_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        location = arguments?.getParcelable("location") ?: defaultLocation


        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            it?.clear()
            it?.uiSettings?.setZoomControlsEnabled(true)
            val options = MarkerOptions().title("BORLABOR").position(LatLng(location.lat, location.lng))
            it?.addMarker(options)
            it?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.lat, location.lng), location.zoom))
        }
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

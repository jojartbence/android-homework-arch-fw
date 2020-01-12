package com.jojartbence.archeologicalfieldwork

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_site.*
import kotlinx.android.synthetic.main.fragment_site.mapView
import kotlinx.android.synthetic.main.site_edit_location_fragment.*


class SiteNavigatorFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this)[SiteNavigatorViewModel::class.java] }

    lateinit var navController: NavController

    var googleMap: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.attachLocation(arguments?.getParcelable("location"))

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
            it?.uiSettings?.isZoomControlsEnabled = true
            val options = MarkerOptions()
                .title("Placemark")
                .snippet("GPS : " + viewModel.getSiteLatLng().toString())
                .draggable(false)
                .position(viewModel.getSiteLatLng())
            it.addMarker(options)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(viewModel.getSiteLatLng(), viewModel.siteLocation.zoom))
            lat.text = "Lat: %.6f".format(viewModel.siteLocation.lat)
            lng.text = "Lng: %.6f".format(viewModel.siteLocation.lng)
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

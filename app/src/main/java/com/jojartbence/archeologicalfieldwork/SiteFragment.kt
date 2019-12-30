package com.jojartbence.archeologicalfieldwork


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jojartbence.helpers.checkLocationPermissions
import com.jojartbence.helpers.readImageFromPath
import com.jojartbence.model.Location
import com.jojartbence.model.SiteModel
import kotlinx.android.synthetic.main.fragment_site.*
import java.text.ParseException
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 */
class SiteFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this)[SiteViewModel::class.java] }

    lateinit var navController: NavController

    lateinit var googleMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.attachArguments(arguments!!.getParcelable("site"), arguments!!.getBoolean("editSite"))

        val visitedSwitchStateObserver = Observer<Boolean> {
            setDateVisitedVisibility(it)
        }

        viewModel.visitedSwitchState.observe(this, visitedSwitchStateObserver)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_site, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.editSite) {
            // load site to UI
            showSite()

            // save the switch state to the viewmodel
            viewModel.visitedSwitchState.value = visited.isChecked
        } else {
            // get the switch state from the viewmodel (e.g. if location had been set, and the view has been recreated)
            visited.isChecked = viewModel.visitedSwitchState.value ?: false

            // set visibility based on restored state
            setDateVisitedVisibility(visited.isChecked)
        }

        showImages()

        if (viewModel.editSite) {
            (activity as AppCompatActivity?)?.supportActionBar?.title = viewModel.site.title
        } else {
            (activity as AppCompatActivity?)?.supportActionBar?.title = "New site"
        }

        imageView1.setOnClickListener { viewModel.doSelectImage(this, viewModel.image1RequestId) }
        imageView2.setOnClickListener { viewModel.doSelectImage(this, viewModel.image2RequestId) }
        imageView3.setOnClickListener { viewModel.doSelectImage(this, viewModel.image3RequestId) }
        imageView4.setOnClickListener { viewModel.doSelectImage(this, viewModel.image4RequestId) }

        visited.setOnClickListener {
            viewModel.visitedSwitchState.value = visited.isChecked
        }

        navController = Navigation.findNavController(view)


        if (!viewModel.editSite) {
            if (checkLocationPermissions(activity as Activity)) {
                var locationService = LocationServices.getFusedLocationProviderClient(activity as Activity)
                locationService.lastLocation.addOnSuccessListener {
                    if (it != null) {
                        viewModel.site.location = (Location(it.latitude, it.longitude, 15f))
                        setCurrentLocationOnMap()
                    }
                }
            }
        }


        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            googleMap = it
            setCurrentLocationOnMap()
            it?.setOnMapClickListener {
                val bundle = bundleOf("location" to viewModel.site.location)
                navController.navigate(R.id.action_siteFragment_to_siteEditLocationFragment, bundle)
            }
        }
    }


    fun setCurrentLocationOnMap() {
        googleMap.clear()
        val newMarker = MarkerOptions().title(viewModel.site.title).position(LatLng(viewModel.site.location.lat, viewModel.site.location.lng))
        googleMap.addMarker(newMarker)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(viewModel.site.location.lat, viewModel.site.location.lng), viewModel.site.location.zoom))
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_site, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.site_delete -> {

                viewModel.doDeleteSite()
                navController.navigateUp()
            }
            R.id.site_save -> {
                if (siteTitle.text.toString().isEmpty()) {
                    Toast.makeText(activity, R.string.toast_enter_site_title, Toast.LENGTH_SHORT).show()
                } else {

                    try {

                        viewModel.doSaveSite(
                            title = siteTitle.text.toString(),
                            description = siteDescription.text.toString(),
                            visited = visited.isChecked,
                            dateVisitedAsString = dateVisited.text.toString(),
                            additionalNotes = addtionalNotes.text.toString()
                        )

                        navController.navigateUp()

                    } catch (e: ParseException) {
                        e.printStackTrace()
                        Toast.makeText( activity, R.string.toast_wrong_date_format, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showSite() {
        val site = viewModel.site

        siteTitle.setText(site.title)
        siteDescription.setText(site.description)
        visited.isChecked = site.visited
        if (site.visited) {
            try {
                dateVisited.setText(SimpleDateFormat("dd.MM.yyyy").format(site.dateVisited ?: SiteModel.defaultDateInCaseOfError))
            } catch (e: Exception) {
                e.printStackTrace()
                dateVisited.setText(SiteModel.defaultDateInCaseOfErrorAsString)
                Toast.makeText( activity, R.string.toast_could_not_load_date, Toast.LENGTH_SHORT).show()
            }
        }
        addtionalNotes.setText(site.additionalNotes)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            viewModel.doActivityResult(requestCode, resultCode, data)
            showImages()
        }
    }


    private fun showImages() {
        val site = viewModel.site

        if (readImageFromPath(activity!!.applicationContext, site.images[0]) != null) {
            imageView1.setImageBitmap(readImageFromPath(activity!!.applicationContext, site.images[0]))
        }
        if (readImageFromPath(activity!!.applicationContext, site.images[1]) != null) {
            imageView2.setImageBitmap(readImageFromPath(activity!!.applicationContext, site.images[1]))
        }
        if (readImageFromPath(activity!!.applicationContext, site.images[2]) != null) {
            imageView3.setImageBitmap(readImageFromPath(activity!!.applicationContext, site.images[2]))
        }
        if (readImageFromPath(activity!!.applicationContext, site.images[3]) != null) {
            imageView4.setImageBitmap(readImageFromPath(activity!!.applicationContext, site.images[3]))
        }
    }


    private fun setDateVisitedVisibility(visible: Boolean) {
        if (visible) {
            dateVisited.visibility = View.VISIBLE
        } else {
            dateVisited.visibility = View.INVISIBLE
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

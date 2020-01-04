package com.jojartbence.archeologicalfieldwork


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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

    var googleMap: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.attachArguments(arguments!!.getParcelable("site"), arguments!!.getBoolean("editSite"))

        val visitedSwitchStateObserver = Observer<Boolean> {
            setDateVisitedVisibility(it)
        }

        viewModel.visitedSwitchState.observe(this, visitedSwitchStateObserver)


        val liveLocationObserver = Observer<Location> {
            setLocationOnMap(it)
        }

        viewModel.liveLocation.observe(this, liveLocationObserver)

        viewModel.initLocationService(activity as Activity)
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


        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            googleMap = it
            setLocationOnMap(viewModel.site.location)
            it?.setOnMapClickListener {
                val bundle = bundleOf("location" to viewModel.site.location)
                navController.navigate(R.id.action_siteFragment_to_siteEditLocationFragment, bundle)
            }
        }
    }


    fun setLocationOnMap(location: Location) {
        googleMap?.clear()
        val newMarker = MarkerOptions().title(viewModel.site.title).position(LatLng(location.lat, location.lng))
        googleMap?.addMarker(newMarker)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.lat, location.lng), location.zoom))
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_site, menu)

        setFavouriteIconImage(menu.findItem(R.id.site_markAsFavourite))
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
                            additionalNotes = addtionalNotes.text.toString(),
                            rating = ratingBar.rating
                        )

                        navController.navigateUp()

                    } catch (e: ParseException) {
                        e.printStackTrace()
                        Toast.makeText( activity, R.string.toast_wrong_date_format, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.site_markAsFavourite -> {
                changeIsFavouriteState(item)
            }
            R.id.site_shareEmail -> {
                viewModel.shareSiteInEmail(this)
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
        ratingBar.rating = site.rating
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            viewModel.doActivityResult(requestCode, resultCode, data)
            showImages()
        }
    }


    private fun showImages() {
        val images = viewModel.site.images

        Glide.with(this).load(images[0]).error(resources.getDrawable(R.drawable.ic_add_photo, context?.theme)).into(imageView1)
        Glide.with(this).load(images[1]).error(resources.getDrawable(R.drawable.ic_add_photo, context?.theme)).into(imageView2)
        Glide.with(this).load(images[2]).error(resources.getDrawable(R.drawable.ic_add_photo, context?.theme)).into(imageView3)
        Glide.with(this).load(images[3]).error(resources.getDrawable(R.drawable.ic_add_photo, context?.theme)).into(imageView4)
    }


    private fun setDateVisitedVisibility(visible: Boolean) {
        if (visible) {
            dateVisited.visibility = View.VISIBLE
        } else {
            dateVisited.visibility = View.INVISIBLE
        }
    }


    private fun changeIsFavouriteState(item: MenuItem) {
        when(viewModel.site.isFavourite) {
            false -> viewModel.site.isFavourite = true
            true -> viewModel.site.isFavourite = false
        }
        setFavouriteIconImage(item)
    }


    private fun setFavouriteIconImage(item: MenuItem) {
        when(viewModel.site.isFavourite) {
            true -> item.icon = resources.getDrawable(android.R.drawable.star_big_on, null)
            false -> item.icon = resources.getDrawable(android.R.drawable.star_big_off, null)
        }
    }


    private fun hideKeyboard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }


    override fun onDestroyView() {
        hideKeyboard()
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

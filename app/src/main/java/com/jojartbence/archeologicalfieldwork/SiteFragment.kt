package com.jojartbence.archeologicalfieldwork


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jojartbence.helpers.readImageFromPath
import com.jojartbence.model.SiteModel
import kotlinx.android.synthetic.main.fragment_site.*
import kotlinx.android.synthetic.main.fragment_site.view.*

/**
 * A simple [Fragment] subclass.
 */
class SiteFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this)[SiteViewModel::class.java] }

    lateinit var navController: NavController


    var editSite: Boolean = false
    lateinit var site: SiteModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        editSite = arguments!!.getBoolean("editSite")
        site = arguments!!.getParcelable("site") ?: SiteModel()

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

        if (editSite) {
            showSiteInEditMode()
        }

        imageView1.setOnClickListener { viewModel.doSelectImage(this, viewModel.image1RequestId) }
        imageView2.setOnClickListener { viewModel.doSelectImage(this, viewModel.image2RequestId) }
        imageView3.setOnClickListener { viewModel.doSelectImage(this, viewModel.image3RequestId) }
        imageView4.setOnClickListener { viewModel.doSelectImage(this, viewModel.image4RequestId) }


        navController = Navigation.findNavController(view)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            it?.clear()
            it?.uiSettings?.setZoomControlsEnabled(true)
            val options = MarkerOptions().title("BORLABOR").position(LatLng(46.959029, 18.934780))
            it?.addMarker(options)
            it?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(46.959029, 18.934780), 15f))

        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_site, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.site_delete -> {
                if (editSite) {
                    viewModel.doDeleteSite(site)
                }
                navController.navigateUp()
            }
            R.id.site_save -> {
                if (siteTitle.text.toString().isEmpty()) {
                    Toast.makeText(activity, R.string.toast_enter_site_title, Toast.LENGTH_SHORT).show()
                } else {
                    if (editSite) {
                        site.title = siteTitle.text.toString()
                        site.description = siteDescription.text.toString()
                        viewModel.doEditSite(site)
                    } else {
                        site.title = siteTitle.text.toString()
                        site.description = siteDescription.text.toString()
                        viewModel.doSaveSite(site)
                    }
                    navController.navigateUp()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun showSiteInEditMode() {
        siteTitle.setText(site.title)
        siteDescription.setText(site.description)

        showImages()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            viewModel.doActivityResult(requestCode, resultCode, data, site)
            showImages()
        }
    }


    fun showImages() {
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

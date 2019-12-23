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

    // TODO: this logic with imageList should be in the ViewModel
    lateinit var imageList: List<Triple<ImageView,Int,String>>

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

        imageList = listOf(
            Triple(imageView1, viewModel.image1RequestId, site.images[0]),
            Triple(imageView2, viewModel.image2RequestId, site.images[1]),
            Triple(imageView3, viewModel.image3RequestId, site.images[2]),
            Triple(imageView4, viewModel.image4RequestId, site.images[3])
        )

        for (imageItem in imageList) {
            imageItem.first.setOnClickListener {viewModel.doSelectImage(this, imageItem.second)}
        }

        navController = Navigation.findNavController(view)
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

        for (imageItem in imageList) {
            val bitmap = readImageFromPath(activity!!.applicationContext, imageItem.third)
            if (bitmap != null) {
                imageItem.first.setImageBitmap(bitmap)
            }
        }
    }
}

package com.jojartbence.archeologicalfieldwork


import android.content.Intent
import android.os.Bundle
import android.view.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        editSite = arguments!!.getBoolean("editSite")


        // TODO: really ugly, should handle the differencies between edit and new sites better
        val tempSite: SiteModel? = arguments!!.getParcelable("site")
        if (tempSite != null) {
            site = tempSite
        } else {
            site = SiteModel()
        }
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
        // TODO: really ugly and code copy. The task is to load an image only if there is a valid image behind its path

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
}

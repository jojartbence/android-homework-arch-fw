package com.jojartbence.archeologicalfieldwork


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
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
    var site: SiteModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        editSite = arguments!!.getBoolean("editSite")
        site = arguments!!.getParcelable("site")
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
                    viewModel.doDeleteSite(site!!)
                    navController.navigateUp()
                }
            }
            R.id.site_save -> {
                if (siteTitle.text.toString().isEmpty()) {
                    Toast.makeText(activity, R.string.toast_enter_site_title, Toast.LENGTH_SHORT).show()
                } else {
                    if (editSite) {
                        site!!.title = siteTitle.text.toString()
                        site!!.description = siteDescription.text.toString()
                        viewModel.doEditSite(site!!)
                    } else {
                        val newSite = SiteModel()
                        newSite.title = siteTitle.text.toString()
                        newSite.description = siteDescription.text.toString()
                        viewModel.doSaveSite(newSite)
                    }
                    navController.navigateUp()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun showSiteInEditMode() {
        siteTitle.setText(site?.title)
        siteDescription.setText(site?.description)
    }
}

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.jojartbence.model.SiteModel
import kotlinx.android.synthetic.main.fragment_site_list.*


class FavouriteSitesList : Fragment(), SiteListener {

    private val viewModel by lazy { ViewModelProviders.of(this)[FavouriteSitesListViewModel::class.java] }

    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourite_sites_list_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = SiteAdapter(viewModel.getFavouriteSites(), this)
        recyclerView.adapter?.notifyDataSetChanged()
    }


    override fun onSiteClick(site: SiteModel) {
        val bundle = bundleOf(
            "site" to site.copy(),
            "editSite" to true
        )
        navController.navigate(R.id.action_favouriteSitesList_to_siteFragment, bundle)
    }

}

package com.jojartbence.archeologicalfieldwork


import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.jojartbence.model.SiteModel
import kotlinx.android.synthetic.main.fragment_site_list.*

/**
 * A simple [Fragment] subclass.
 */
class SiteListFragment : Fragment(), SiteListener, SearchView.OnQueryTextListener {

    private val viewModel by lazy { ViewModelProviders.of(this)[SiteListViewModel::class.java] }

    lateinit var navController: NavController


    private val filteredSitesObserver = Observer<List<SiteModel>> {
        recyclerView.adapter = SiteAdapter(it, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_site_list, container, false)
    }

    /* TODO: after updating the first image of a site, the old image will appear in the site list.
             Reason: upload images to firebase store (and the updating URL) is slower than
             loading images via the adapter.
     */


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerView.layoutManager = layoutManager

        viewModel.filteredSites.observe(this, filteredSitesObserver)

        recyclerView.adapter?.notifyDataSetChanged()

        searchView.setOnQueryTextListener(this)
    }


    override fun onSiteClick(site: SiteModel) {
        val bundle = bundleOf(
            "site" to site.copy(),
            "editSite" to true
        )
        navController.navigate(R.id.action_siteListFragment_to_siteFragment, bundle)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        viewModel.filterSitesByTitle(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        viewModel.filterSitesByTitle(newText)
        return true
    }
}

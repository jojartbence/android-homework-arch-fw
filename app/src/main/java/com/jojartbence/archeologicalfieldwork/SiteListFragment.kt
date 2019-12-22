package com.jojartbence.archeologicalfieldwork


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.jojartbence.model.SiteModel
import kotlinx.android.synthetic.main.fragment_site_list.*

/**
 * A simple [Fragment] subclass.
 */
class SiteListFragment : Fragment(), SiteListener {

    private val viewModel by lazy { ViewModelProviders.of(this)[SiteListViewModel::class.java] }

    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel.createDatabase(activity!!.applicationContext)
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_site_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = SiteAdapter(viewModel.getSites(), this)
        recyclerView.adapter?.notifyDataSetChanged()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_sitelist, menu)
    }


    override fun onSiteClick(site: SiteModel) {
        val bundle = bundleOf(
            "site" to site,
            "editSite" to true
        )
        navController.navigate(R.id.action_siteListFragment_to_siteFragment, bundle)
    }
}

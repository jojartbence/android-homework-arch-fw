package com.jojartbence.archeologicalfieldwork


import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_site_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        hideKeyboard()

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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_add -> navController.navigate(R.id.action_siteListFragment_to_siteFragment)

            R.id.item_logout -> {
                viewModel.doLogOut()
                navController.navigateUp()
            }

            R.id.item_settings -> {
                navController.navigate(R.id.action_siteListFragment_to_settingsFragment)
            }

            android.R.id.home -> {
                // Close the app
                activity?.finishAndRemoveTask()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun hideKeyboard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}

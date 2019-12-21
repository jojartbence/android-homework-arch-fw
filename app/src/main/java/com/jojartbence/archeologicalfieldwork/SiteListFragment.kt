package com.jojartbence.archeologicalfieldwork


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jojartbence.model.SiteModel
import kotlinx.android.synthetic.main.fragment_site_list.*

/**
 * A simple [Fragment] subclass.
 */
class SiteListFragment : Fragment(), SiteListener {

    private val viewModel by lazy { ViewModelProviders.of(this)[SiteListViewModel::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel.createDatabase(activity!!.applicationContext)

        return inflater.inflate(R.layout.fragment_site_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity!!.applicationContext)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = SiteAdapter(viewModel.getSites(), this)
        recyclerView.adapter?.notifyDataSetChanged()
    }


    override fun onSiteClick(site: SiteModel) {
        viewModel.doEditSite(site)
    }
}

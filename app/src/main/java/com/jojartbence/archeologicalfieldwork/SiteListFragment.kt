package com.jojartbence.archeologicalfieldwork


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

/**
 * A simple [Fragment] subclass.
 */
class SiteListFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this)[SiteListViewModel::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel.createDatabase(activity!!.applicationContext)

        return inflater.inflate(R.layout.fragment_site_list, container, false)
    }


}

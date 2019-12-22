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
class SiteFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this)[SiteViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_site, container, false)
    }


}

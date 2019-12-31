package com.jojartbence.archeologicalfieldwork

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController


class FavouriteSitesList : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this)[FavouriteSitesListViewModel::class.java] }

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourite_sites_list_fragment, container, false)
    }

}

package com.jojartbence.archeologicalfieldwork


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this)[SettingsViewModel::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountName.text = getString(R.string.settings_account_name) + " " + viewModel.getLoggedInAs()
        totalSites.text = getString(R.string.settings_total_sites) + " " + viewModel.getTotalSites()
        visitedSites.text = getString(R.string.settings_visited_sites) + " " + viewModel.getVisitedSites()
    }


}

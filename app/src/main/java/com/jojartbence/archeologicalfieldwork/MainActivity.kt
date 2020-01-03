package com.jojartbence.archeologicalfieldwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.jojartbence.model.SiteRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val topLevelDestinations = setOf(R.id.nav_mapOfAllSites, R.id.nav_favouriteSites, R.id.nav_settings, R.id.nav_siteList)
        appBarConfig = AppBarConfiguration.Builder(topLevelDestinations).setDrawerLayout(drawer_layout).build()



        navController = Navigation.findNavController(this, R.id.fragment)
        setupActionBarWithNavController(navController, appBarConfig)
        nav_view.setupWithNavController(navController)

        nav_view.setNavigationItemSelectedListener(this)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                doLogOut()
            }
            else -> {
                // TODO: the navigation drawer's default navigate function should be called here, which also does some addition stuff
                navController.navigate(item.itemId)
            }

        }

        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    private fun doLogOut() {
        FirebaseAuth.getInstance().signOut()
        SiteRepository.clear()
        navController.navigateUp()
    }
}

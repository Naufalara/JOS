package com.rizky.journeyonsolo

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.rizky.journeyonsolo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController

        // Check login status and navigate accordingly
        if (isUserLoggedIn()) {
            navController.navigate(R.id.navigation_home)
        } else {
            navController.navigate(R.id.onBoardingFragment)
        }

        navView.setupWithNavController(navController)

        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.navigation_maps,
                R.id.onBoardingFragment,
                R.id.registerFragment,
                R.id.loginFragment,
                R.id.searchResultFragment,
                R.id.detailFragment,
                R.id.detailMapsFragment -> {
                    navView.visibility = View.GONE
                } else -> navView.visibility = View.VISIBLE
            }
        }

        navController.addOnDestinationChangedListener(listener)
    }

    private fun isUserLoggedIn(): Boolean {
        return false
    }
}
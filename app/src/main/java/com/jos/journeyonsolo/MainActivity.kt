package com.jos.journeyonsolo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jos.journeyonsolo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView
        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController

        navView.setupWithNavController(navController)
        auth = FirebaseAuth.getInstance()

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

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                navController.navigate(R.id.navigation_home)
            } else {
                navController.navigate(R.id.onBoardingFragment)
            }
        }
    }
}
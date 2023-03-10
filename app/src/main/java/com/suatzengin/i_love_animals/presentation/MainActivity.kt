package com.suatzengin.i_love_animals.presentation

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.suatzengin.i_love_animals.R
import com.suatzengin.i_love_animals.databinding.ActivityMainBinding
import com.suatzengin.i_love_animals.util.checkLocationPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        //binding.bottomAppBar.setupWithNavController(navController)
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mapsFragment, R.id.adDetailFragment -> {
                    binding.bottomAppBar.visibility = View.INVISIBLE
                    binding.postAd.visibility = View.INVISIBLE
                }

                R.id.adListFragment, R.id.profileFragment -> {
                    binding.bottomAppBar.visibility = View.VISIBLE
                    binding.postAd.visibility = View.VISIBLE
                }
            }
        }
        binding.postAd.setOnClickListener {
            checkLocationPermission(
                functionIfGranted = {
                    navController.navigate(R.id.mapsFragment)
                },
                functionIfDenied = {
                    requestPermission.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            )
        }
        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.adListFragment)
                    true
                }

                R.id.profile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }

                else -> false
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        permission.entries.forEach {
            if (it.value) {
                //granted
                navController.navigate(R.id.mapsFragment)
            } else {
                // Permission is denied
                shouldShowRequestPermissionRationale(
                    it.key
                )
                Snackbar
                    .make(binding.coordinatorLayout, "Ayarlardan izin ver", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }
}

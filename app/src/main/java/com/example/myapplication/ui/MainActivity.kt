package com.example.myapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        // 3) Tell the ActionBar which destinations are “top level”
        //    (so no Up arrow shows on them)
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.menu_popular,
                R.id.menu_favorite,
                R.id.menu_search,
                R.id.menu_top_rated
            )
        )
        setupActionBarWithNavController(navController, appBarConfig)

        // 4) Hook up your BottomNavigationView
        binding.bottomNav.setupWithNavController(navController)
    }

}
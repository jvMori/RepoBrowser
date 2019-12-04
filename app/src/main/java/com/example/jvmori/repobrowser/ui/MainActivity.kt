package com.example.jvmori.repobrowser.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.jvmori.repobrowser.R
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()
    }

    private fun setupNavigation() {
        val controller = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, controller)
    }

}

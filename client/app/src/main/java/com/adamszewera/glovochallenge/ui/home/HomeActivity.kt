package com.adamszewera.glovochallenge.ui.home

import android.os.Bundle
import com.adamszewera.glovochallenge.core.ui.BaseActivity

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment.newInstance())
        }
    }

}
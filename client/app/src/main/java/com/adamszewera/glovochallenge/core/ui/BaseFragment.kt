package com.adamszewera.glovochallenge.core.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.adamszewera.glovochallenge.App
import com.adamszewera.glovochallenge.di.components.DaggerFragmentComponent
import com.adamszewera.glovochallenge.di.components.FragmentComponent

abstract class BaseFragment : Fragment() {

    abstract fun layoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getInstance().getAppComponent().inject(this)
    }

    fun getFragmentComponent(): FragmentComponent {
        return DaggerFragmentComponent.builder()
            .appComponent(App.getInstance().getAppComponent())
            .build()
    }
}
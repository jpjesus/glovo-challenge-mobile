package com.adamszewera.glovochallenge.di.components

import com.adamszewera.glovochallenge.di.modules.ViewModelModule
import com.adamszewera.glovochallenge.di.qualifiers.PerActivity
import com.adamszewera.glovochallenge.ui.home.HomeFragment
import dagger.Component

@PerActivity
@Component(
    dependencies = [AppComponent::class],
    modules = [
        ViewModelModule::class
    ]
)
interface FragmentComponent {


    fun inject(fragment: HomeFragment)

}
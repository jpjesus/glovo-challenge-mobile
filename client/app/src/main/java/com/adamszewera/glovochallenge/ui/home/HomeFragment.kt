package com.adamszewera.glovochallenge.ui.home

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.adamszewera.glovochallenge.R
import com.adamszewera.glovochallenge.core.ui.BaseFragment
import com.adamszewera.glovochallenge.data.models.City
import com.adamszewera.glovochallenge.databinding.FragmentHomeBinding
import com.adamszewera.glovochallenge.util.ConvexHull
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Predicate
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val REQUEST_CODE_LOCATION = 1992

class HomeFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mContext : Context

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var map: GoogleMap

    private lateinit var mapView: MapView

    private var mapNeedsUpdating : Boolean = false

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
        mContext = activity as Context
        setHasOptionsMenu(true)
    }

    override fun layoutId(): Int = R.layout.fragment_home


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)


        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater, R.layout.fragment_home, container, false
        ).apply {
            viewModel = homeViewModel
        }
        binding.setLifecycleOwner(this)


        homeViewModel.cities.observe(this, Observer<List<City>> { cities -> showAllCities(cities, true) } )

        homeViewModel.currentCity.observe(this, Observer<City> { showCurrentCity(it) })

        homeViewModel.firstAccess.observe(this, Observer<Boolean> { showCustomDialog() })

        mapView = binding.root.findViewById<MapView>(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.loadData()
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }











    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.search_menu, menu)
    }











    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Helper Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun showCustomDialog() {
        val dialog = Dialog(this.activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_tracing)
        dialog.setCancelable(true)

        var lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        var yesBtn = dialog.findViewById<Button>(R.id.yes_btn)
        var noBtn = dialog.findViewById<Button>(R.id.no_btn)

        yesBtn.setOnClickListener {
            enableLocation()
            dialog.dismiss()
        }

        noBtn.setOnClickListener {
            dialog.dismiss()
            homeViewModel.loadCities()
        }

        dialog.show()
        dialog.window.attributes = lp
    }


    private fun showAllCities(cities: List<City>?, updateCamera: Boolean) {
        map.clear()
        with(map) {
            var boundsBuilder = LatLngBounds.Builder()
            cities?.forEach {
                // the first point of the polygon is a "good" approximation when seeing the whole world
                boundsBuilder.include(it.working_area[0])
                addMarker(
                    MarkerOptions()
                        .position(it.working_area[0])
                        .snippet(it.code)
                        .title(it.name)
                )
            }

            if (updateCamera) {
                moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50))
            }
        }

    }


    private fun showCurrentCity(city: City) {
        // todo: camera animation
        var boundsBuilder = LatLngBounds.Builder()
        city.working_area.forEach {
            boundsBuilder.include(it)
        }
        map.moveCamera(
            CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50)
        )

        // clear other pins for efficiency
        map.clear()

        showCityWorkingArea(city.working_area)

    }


    private fun showCityWorkingArea(workingArea: List<LatLng>) {
        val fillColor = ContextCompat.getColor(mContext, R.color.polygon_fill_color)
        val simplifiedPolygon = ConvexHull.makeHull(workingArea)
        map.addPolygon(PolygonOptions().apply {
            addAll(simplifiedPolygon)
            fillColor(fillColor)
        })
    }

    private fun showWorkingAreas(cities: List<City>?) {
//        map.clear()
        val disposable =
        Observable.just(cities)
            .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .flatMapIterable { it }
            .filter { city -> map.projection.visibleRegion.latLngBounds.contains(city.working_area[0])}
            .toList()
            .subscribe(
                { it.forEach { city -> showCityWorkingArea(city.working_area) } },
                { Timber.e(it) }
            )
    }






    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                       google maps camera movement
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onCameraIdle() {
        val zoom = map.cameraPosition.zoom
        if (zoom < 5) {
            showAllCities(homeViewModel.cities.value, false)
        } else if (zoom > 8){
            showWorkingAreas(homeViewModel.cities.value)
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Permissions
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    private fun enableLocation() {
        if (hasLocationPermission()) {
//            map.is
        } else {
            val permissionRequest = PermissionRequest.Builder(
                this,
                REQUEST_CODE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ).build()
            EasyPermissions.requestPermissions(permissionRequest)
        }
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  OnMarkerClickListener
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onMarkerClick(marker: Marker?): Boolean {
        // move camera to the city and center the camera on all the points + padding that contain the whole ara of the city
        val cityCode = marker?.snippet
        if (cityCode != null) {
            homeViewModel.loadCurrentCity(cityCode)
        }
        return true
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  OnMapReadyCallback
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return

        map.setOnMarkerClickListener(this)
        map.setOnCameraIdleListener(this)
        with(map.uiSettings) {
            isMyLocationButtonEnabled = true
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
        }
    }








}
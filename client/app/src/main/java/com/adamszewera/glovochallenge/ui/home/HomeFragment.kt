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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import javax.inject.Inject

const val REQUEST_CODE_LOCATION = 1992

//    android:id="@+id/info_code_tv"
//    android:id="@+id/info_name_tv"
//    android:id="@+id/info_country_code_tv"
//    android:id="@+id/info_currency_tv"
//    android:id="@+id/info_enabled_tv"
//    android:id="@+id/info_busy_tv"
//    android:id="@+id/info_time_zone_tv"
//    android:id="@+id/info_language_code_tv"
//    android:id="@+id/info_working_area_tv"

class HomeFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnCameraMoveListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mContext : Context

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var map: GoogleMap

    private lateinit var mapView: MapView

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


        val fillColor = ContextCompat.getColor(mContext, R.color.polygon_fill_color)
        homeViewModel.cities.observe(this, object: Observer<List<City>> {
            override fun onChanged(cities: List<City>?) {
                cities?.forEach {
                    val city = it
                    val simplifiedPolygon = ConvexHull.makeHull(city.working_area)
                    map.addPolygon(PolygonOptions().apply {
                        addAll(simplifiedPolygon)
                        fillColor(fillColor)
                    })

                }
            }
        }
        )

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











    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Helper Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////



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
        }

        dialog.show()
        dialog.window.attributes = lp
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Permissions
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onCameraMove() {
        // use debounce with rxjava to limit the number of events
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


    // todo: remove
//    val BARCELONA = LatLng(41.383333, 2.183333)
//    val ZOOM_LEVEL = 13f

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  OnMapReadyCallback
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return

        with(map) {
//            moveCamera(CameraUpdateFactory.newLatLngZoom(BARCELONA, ZOOM_LEVEL))
//            addMarker(MarkerOptions().position(BARCELONA))
        }
        with(map.uiSettings) {
            isMyLocationButtonEnabled = true
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
        }
//        map.moveCamera(CameraUpdateFactory.newLatLng(BARCELONA))

        homeViewModel.loadCities()
    }








}
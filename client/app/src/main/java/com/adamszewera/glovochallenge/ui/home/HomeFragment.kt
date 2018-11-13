package com.adamszewera.glovochallenge.ui.home

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.adamszewera.glovochallenge.R
import com.adamszewera.glovochallenge.core.ui.BaseFragment
import com.adamszewera.glovochallenge.data.models.City
import com.adamszewera.glovochallenge.databinding.FragmentHomeBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import timber.log.Timber
import javax.inject.Inject

const val REQUEST_CODE_LOCATION = 1992

class HomeFragment : BaseFragment(), OnMapReadyCallback {

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
    }

    override fun layoutId(): Int = R.layout.fragment_home


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)


        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater, R.layout.fragment_home, container, false
        ).apply {
            viewModel = homeViewModel
        }


        val fillColor = mContext.resources.getColor(R.color.polygonFillColor)
        homeViewModel.cities.observe(this, object: Observer<List<City>> {
            override fun onChanged(cities: List<City>?) {
                cities?.forEach {
                    val city = it
                    map.addPolygon(PolygonOptions().apply {
                        addAll(city.working_area)
                        fillColor(fillColor)
                    })

                }
            }
        }
        )

        mapView = binding.root.findViewById<MapView>(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo: show on first open
//        homeViewModel.loadCities()


    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

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
    val SYDNEY = LatLng(-33.862, 151.21)
    val ZOOM_LEVEL = 13f

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  OnMapReadyCallback
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onMapReady(googleMap: GoogleMap?) {
        Timber.d("on map ready: before")
        map = googleMap ?: return


        Timber.d("on map ready: after")
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, ZOOM_LEVEL))
            addMarker(MarkerOptions().position(SYDNEY))
        }
        with(map.uiSettings) {
            isMyLocationButtonEnabled = true
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY))

        homeViewModel.loadCities()
    }








}
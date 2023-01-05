package com.suatzengin.i_love_animals.presentation.post_ad


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.suatzengin.i_love_animals.R
import com.suatzengin.i_love_animals.databinding.FragmentMapsBinding
import com.suatzengin.i_love_animals.domain.model.MyLocation
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private val viewModel: PostAdViewModel by viewModels()
    private var postAdSheet = PostAdSheetFragment(null)
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private val defaultLocation = LatLng(-34.0, 151.0)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapsBinding.inflate(inflater, container, false)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        viewModel.location.observe(viewLifecycleOwner) {
            //PostAdSheetFragment(address = it)
            postAdSheet = PostAdSheetFragment(myLocation = it)
        }

        binding.btn.setOnClickListener {
            getDeviceLocation()
        }

    }

    override fun onStart() {
        super.onStart()
        val supportFragmentManager =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportFragmentManager.getMapAsync(this)

    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    @SuppressLint("MissingPermission")
    private fun isGpsProviderEnabled() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (
            !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
            !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {

            MaterialAlertDialogBuilder(requireContext())
                .setMessage("You should turn on the location!")
                .setPositiveButton(
                    "OK"
                ) { _, _ ->
                    this.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton("Cancel", null)
                .show()
            mMap.isMyLocationEnabled = false
            mMap.uiSettings.isMyLocationButtonEnabled = false
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        updateLocationUI()

        mMap.apply {
            setPadding(16, 64, 16, 16)
            setOnMapLongClickListener {
                addMarker(
                    MarkerOptions().position(it).draggable(true)
                )
                animateCamera(CameraUpdateFactory.newLatLngZoom(it, DEFAULT_ZOOM.toFloat()))

                val myLocation = MyLocation(
                    address = getAddressLineFromLatLng(it.latitude, it.longitude),
                    it.latitude, it.longitude
                )
                viewModel.addLocation(myLocation)
            }

            setOnMapClickListener {
                postAdSheet.showNow(
                    parentFragmentManager, "BOTTOMSHEET"
                )
            }
            setOnMarkerDragListener(object : OnMarkerDragListener {
                override fun onMarkerDrag(p0: Marker) {}
                override fun onMarkerDragEnd(marker: Marker) {

                    val address = getAddressLineFromLatLng(
                        marker.position.latitude,
                        marker.position.longitude
                    )
                    val myLocation = MyLocation(
                        address = address,
                        marker.position.latitude, marker.position.longitude
                    )
                    viewModel.addLocation(myLocation)

                }

                override fun onMarkerDragStart(p0: Marker) {}
            })

            isGpsProviderEnabled()
        }

    }

    private fun getAddressLineFromLatLng(latitude: Double, longitude: Double): String {
        val geo = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geo.getFromLocation(
            latitude,
            longitude,
            1
        )
        return addresses[0].getAddressLine(0)
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        try {

            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    lastKnownLocation = task.result

                    if (lastKnownLocation != null) {
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude),
                                DEFAULT_ZOOM.toFloat()
                            )
                        )

                        val address = getAddressLineFromLatLng(
                            lastKnownLocation!!.latitude,
                            lastKnownLocation!!.longitude
                        )
                        val myLocation = MyLocation(
                            address = address,
                            lastKnownLocation!!.latitude, lastKnownLocation!!.longitude
                        )
                        viewModel.addLocation(myLocation)
                        if (address.isEmpty()) println("waiting for location")
                        else println("addressLine: $address")
                    } else {
                        Log.d("TAG", "Current location is null. Using defaults.")

                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                defaultLocation, DEFAULT_ZOOM.toFloat()
                            )
                        )
                        mMap.uiSettings.isMyLocationButtonEnabled = true
                    }
                }
            }

        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    companion object {
        private const val DEFAULT_ZOOM = 20
    }
}

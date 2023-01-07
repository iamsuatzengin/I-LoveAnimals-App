package com.suatzengin.i_love_animals.presentation.ad_detail

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.suatzengin.i_love_animals.R
import com.suatzengin.i_love_animals.databinding.FragmentAdDetailBinding
import com.suatzengin.i_love_animals.util.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AdDetailFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    val args: AdDetailFragmentArgs by navArgs()
    private val viewModel: AdDetailViewModel by viewModels()
    private lateinit var binding: FragmentAdDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdDetailBinding.inflate(inflater, container, false)

        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val supportFragmentManager = childFragmentManager.findFragmentById(
                R.id.map_detail
            ) as SupportMapFragment
            supportFragmentManager.getMapAsync(this)
        } else {
            requestPermission.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ad = args.advertisement
        binding.btnComplete.setOnClickListener {
            val ad = args.advertisement.copy(status = !(args.advertisement.status))
            viewModel.changeStatus(ad.id!!, ad.status)
        }

        if (args.advertisement.status) binding.btnComplete.isEnabled = false

        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { event ->
                when(event){
                    is UiEvent.ShowMessage -> {
                        Snackbar.make(view,event.message,Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        permission.entries.forEach {
            if (it.value) {
                //granted
                val supportFragmentManager = childFragmentManager.findFragmentById(
                    R.id.map_detail
                ) as SupportMapFragment
                supportFragmentManager.getMapAsync(this)
            } else {
                // Permission is denied
                shouldShowRequestPermissionRationale(
                    it.key
                )
                Snackbar
                    .make(requireView(), "Ayarlardan izin ver", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val location = args.advertisement.location
        val latLng = LatLng(location?.latitude!!, location.longitude!!)
        mMap.apply {

            addMarker(
                MarkerOptions()
                    .title(location.address)
                    .position(latLng)
            )
            animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng, 20f
                )
            )
            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
        }
    }

}
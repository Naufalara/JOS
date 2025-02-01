package com.jos.journeyonsolo.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jos.journeyonsolo.R
import com.jos.journeyonsolo.databinding.FragmentDetailMapsBinding

class DetailMapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentDetailMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        val dataName = DetailMapsFragmentArgs.fromBundle(arguments as Bundle).name
        val dataLatitude = DetailMapsFragmentArgs.fromBundle(arguments as Bundle).latitude.toDouble()
        val dataLongitude = DetailMapsFragmentArgs.fromBundle(arguments as Bundle).longitude.toDouble()

        getAllDestinationLocation(dataLatitude, dataLongitude, dataName)
    }

    private fun getAllDestinationLocation(latitude: Double, longitude: Double, name: String){
        val latLng = LatLng(latitude, longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(name)
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
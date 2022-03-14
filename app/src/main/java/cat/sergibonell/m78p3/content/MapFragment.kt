package cat.sergibonell.m78p3.content

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cat.sergibonell.m78p3.R
import cat.sergibonell.m78p3.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

const val REQUEST_CODE_LOCATION = 100

class MapFragment: Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener {
    lateinit var binding: FragmentMapBinding
    private val viewModel: MapViewModel by activityViewModels()
    lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater)
        createMap()
        return binding.root
    }

    fun createMap(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setListeners()
        loadMarkers()
        enableLocation()
    }

    // COPIED CODE

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun enableLocation(){
        if(!::map.isInitialized) return
        if(isLocationPermissionGranted())
            map.isMyLocationEnabled = true
        else
            requestLocationPermission()
    }

    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(requireContext(), "Ves a la pantalla de permisos de l’aplicació i habilita el de Geolocalització", Toast.LENGTH_SHORT).show()
        }
        else{
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            }
            else{
                Toast.makeText(requireContext(), "Accepta els permisos de geolocalització",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createMarker(coordinates: LatLng){
        val markerOptions = MarkerOptions().position(coordinates).title("ITB")

        map.addMarker(markerOptions)
        viewModel.markerList.observe(viewLifecycleOwner, Observer { list ->
            list.add(markerOptions)
        })

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            5000, null)
    }

    fun createCustomMarker(coordinates: LatLng){
        findNavController().navigate(R.id.action_mapFragment_to_addMarkerFragment)
    }

    fun loadMarkers(){
        viewModel.markerList.observe(viewLifecycleOwner, Observer { list ->
            for (marker in list)
                map.addMarker(marker)
        })
    }

    fun setListeners(){
        map.setOnMapLongClickListener(this)
        map.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(marker: Marker) {
        Toast.makeText(context, "Pressed", Toast.LENGTH_LONG).show()
    }

    override fun onMapLongClick(pos: LatLng) {
        createCustomMarker(pos)
    }
}
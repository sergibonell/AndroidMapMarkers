package cat.sergibonell.m78p3.content.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import cat.sergibonell.m78p3.R
import cat.sergibonell.m78p3.content.detail.DetailViewModel
import cat.sergibonell.m78p3.data.PostData
import cat.sergibonell.m78p3.databinding.FragmentMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.*

const val REQUEST_CODE_LOCATION = 100

class MapFragment: Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowLongClickListener {
    lateinit var binding: FragmentMapBinding
    private val viewModel: MapViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by activityViewModels()
    lateinit var map: GoogleMap
    private val db = FirebaseFirestore.getInstance()

    override fun onResume() {
        super.onResume()
        detailViewModel.clearAll()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater)

        viewModel.sessionEmail = activity?.intent?.getStringExtra("email").toString()
        viewModel.addEventListener()

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
        observeMarkers()
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
        if(!isLocationPermissionGranted())
            requestLocationPermission()
        else
            map.isMyLocationEnabled = true
    }

    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(requireContext(), "Ves a la pantalla de permisos de l’aplicació i habilita el de Geolocalització", Toast.LENGTH_SHORT).show()
        }
        else{
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION
            )
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

    fun createCustomMarker(coordinates: LatLng){
        detailViewModel.currentLatitude = coordinates.latitude
        detailViewModel.currentLongitude = coordinates.longitude
        findNavController().navigate(MapFragmentDirections.actionMapFragmentToAddMarkerFragment())
    }

    fun observeMarkers(){
        viewModel.postLiveData.observe(viewLifecycleOwner){
            map.clear()
            for(post in it){
                val position = LatLng(post.latitude!!, post.longitude!!)
                val option = MarkerOptions().position(position).title(post.title)
                map.addMarker(option)?.tag = post.id
            }
        }
    }

    fun setListeners(){
        map.setOnMapLongClickListener(this)
        map.setOnInfoWindowClickListener(this)
        map.setOnInfoWindowLongClickListener(this)
        binding.floatingActionButton.setOnClickListener { findNavController().navigate(R.id.action_mapFragment_to_markerListFragment) }
    }

    override fun onInfoWindowClick(marker: Marker) {
        val post = viewModel.getMarker(marker.tag.toString())
        detailViewModel.setData(post)
        findNavController().navigate(MapFragmentDirections.actionMapFragmentToViewMarkerFragment())
    }

    override fun onMapLongClick(pos: LatLng) {
        createCustomMarker(pos)
    }

    override fun onInfoWindowLongClick(marker: Marker) {
        val post = viewModel.getMarker(marker.tag.toString())
        detailViewModel.setData(post)
        detailViewModel.edit = true

        findNavController().navigate(R.id.action_mapFragment_to_editMarkerFragment)
    }


}
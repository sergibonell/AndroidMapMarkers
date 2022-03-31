package cat.sergibonell.m78p3.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import cat.sergibonell.m78p3.R
import cat.sergibonell.m78p3.data.PostData
import cat.sergibonell.m78p3.databinding.FragmentAddMarkerBinding
import com.google.android.gms.maps.model.LatLng

class AddMarkerFragment: Fragment() {
    lateinit var binding: FragmentAddMarkerBinding
    private val viewModel: MapViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMarkerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButton.setOnClickListener { openCamera() }
        binding.button.setOnClickListener { saveMarker() }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }
    }

    fun openCamera(){
        findNavController().navigate(R.id.action_addMarkerFragment_to_cameraFragment)
    }

    fun saveMarker(){
        val title = binding.editTextTextPersonName.text.toString()
        val description = binding.editTextTextPersonName.text.toString()
        val category = binding.spinner.selectedItem.toString()
        val position = arguments?.getParcelable<LatLng>("coords")!!
        val data = PostData(title, description, category, "", position)

        viewModel.addMarkerList(data)
        findNavController().navigate(R.id.action_addMarkerFragment_to_mapFragment)
    }
}
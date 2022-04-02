package cat.sergibonell.m78p3.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import cat.sergibonell.m78p3.R
import cat.sergibonell.m78p3.data.PostData
import cat.sergibonell.m78p3.databinding.FragmentAddMarkerBinding
import com.google.android.gms.maps.model.LatLng
import java.util.*

class EditMarkerFragment: Fragment() {
    lateinit var binding: FragmentAddMarkerBinding
    private val viewModel: MapViewModel by activityViewModels()
    lateinit var oldData: PostData

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
        binding.button.setOnClickListener { updateMarker() }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }

        oldData = arguments?.getParcelable<PostData>("data")!!
        binding.titleText.setText(oldData.title)
        binding.descriptionText.setText(oldData.description)
        val index = resources.getStringArray(R.array.category_list).indexOf(oldData.category)
        binding.spinner.setSelection(index)
    }

    fun openCamera(){
        findNavController().navigate(R.id.action_addMarkerFragment_to_cameraFragment)
    }

    fun updateMarker(){
        val title = binding.titleText.text.toString()
        val description = binding.descriptionText.text.toString()
        val category = binding.spinner.selectedItem.toString()
        val position = oldData.position
        val data = PostData(title, description, category, "", position)

        viewModel.updateMarker(data, oldData)
        findNavController().navigate(R.id.action_editMarkerFragment_to_markerListFragment)
    }
}
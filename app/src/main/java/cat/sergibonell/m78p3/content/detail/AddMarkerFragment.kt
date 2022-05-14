package cat.sergibonell.m78p3.content.detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import cat.sergibonell.m78p3.R
import cat.sergibonell.m78p3.content.map.MapViewModel
import cat.sergibonell.m78p3.data.PostData
import cat.sergibonell.m78p3.databinding.FragmentAddMarkerBinding

class AddMarkerFragment: Fragment() {
    lateinit var binding: FragmentAddMarkerBinding
    private val viewModel: MapViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by activityViewModels()

    private lateinit var titleText: EditText
    private lateinit var descriptionText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var cameraButton: ImageButton
    private lateinit var doneButton: Button

    override fun onResume() {
        super.onResume()
        detailViewModel.printAll()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMarkerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleText = binding.titleText
        descriptionText = binding.descriptionText
        categorySpinner = binding.spinner
        cameraButton = binding.imageButton
        doneButton = binding.button

        cameraButton.setOnClickListener { openCamera() }
        doneButton.setOnClickListener { saveMarker() }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }

        if(detailViewModel.localPhoto != "")
            setPhoto()

        titleText.setText(detailViewModel.currentTitle)
        descriptionText.setText(detailViewModel.currentDescription)
        val index = resources.getStringArray(R.array.category_list).indexOf(detailViewModel.currentCategory)
        categorySpinner.setSelection(index)
    }

    fun openCamera(){
        detailViewModel.currentTitle = titleText.text.toString()
        detailViewModel.currentDescription = descriptionText.text.toString()
        detailViewModel.currentCategory = categorySpinner.selectedItem.toString()

        findNavController().navigate(R.id.action_addMarkerFragment_to_cameraFragment)
    }

    fun saveMarker(){
        if(binding.titleText.text.isNotBlank()) {
            val title = titleText.text.toString()
            val description = descriptionText.text.toString()
            val category = categorySpinner.selectedItem.toString()
            val photo = detailViewModel.localPhoto
            val latitude = detailViewModel.currentLatitude
            val longitude = detailViewModel.currentLongitude
            val data = PostData(
                title = title,
                description = description,
                category = category,
                photoDirectory = photo,
                latitude = latitude,
                longitude = longitude
            )

            viewModel.addMarker(data)
            findNavController().navigate(R.id.action_addMarkerFragment_to_mapFragment)
        }else {
            Toast.makeText(requireContext(), getString(R.string.empty_title), Toast.LENGTH_SHORT).show()
        }
    }

    fun setPhoto(){
        cameraButton.setImageURI(Uri.parse(detailViewModel.localPhoto))
        cameraButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fui_transparent))
    }
}
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
import cat.sergibonell.m78p3.ImageSelectListener
import cat.sergibonell.m78p3.R
import cat.sergibonell.m78p3.content.map.MapViewModel
import cat.sergibonell.m78p3.data.PostData
import cat.sergibonell.m78p3.databinding.FragmentAddMarkerBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class EditMarkerFragment: Fragment(), ImageSelectListener {
    lateinit var binding: FragmentAddMarkerBinding
    private val viewModel: MapViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by activityViewModels()

    private lateinit var titleText: EditText
    private lateinit var descriptionText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var cameraButton: ImageButton
    private lateinit var doneButton: Button

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
        doneButton.setOnClickListener { updateMarker() }

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
        else if(detailViewModel.currentPhoto != "")
            getPhoto(detailViewModel.currentPhoto)

        titleText.setText(detailViewModel.currentTitle)
        descriptionText.setText(detailViewModel.currentDescription)
        val index = resources.getStringArray(R.array.category_list).indexOf(detailViewModel.currentCategory)
        categorySpinner.setSelection(index)
    }

    fun openCamera(){
        detailViewModel.currentTitle = titleText.text.toString()
        detailViewModel.currentDescription = descriptionText.text.toString()
        detailViewModel.currentCategory = categorySpinner.selectedItem.toString()

        //findNavController().navigate(R.id.action_editMarkerFragment_to_cameraFragment)
        MediaSelectDialogFragment(this).show(childFragmentManager, MediaSelectDialogFragment.TAG)
    }

    fun updateMarker(){
        if(binding.titleText.text.isNotBlank()) {
            val id = detailViewModel.currentId
            val title = titleText.text.toString()
            val description = descriptionText.text.toString()
            val category = categorySpinner.selectedItem.toString()
            val photo = detailViewModel.localPhoto
            val latitude = detailViewModel.currentLatitude
            val longitude = detailViewModel.currentLongitude
            val data = PostData(
                id = id,
                title = title,
                description = description,
                category = category,
                photoDirectory = photo,
                latitude = latitude,
                longitude = longitude
            )

            viewModel.editMarker(data)
            findNavController().navigate(R.id.action_editMarkerFragment_to_markerListFragment)
        }else {
            Toast.makeText(requireContext(), getString(R.string.empty_title), Toast.LENGTH_SHORT).show()
        }
    }

    fun getPhoto(uri: String){
        val storage = FirebaseStorage.getInstance().reference.child(uri)
        val localFile = File.createTempFile("temp", "")
        storage.getFile(localFile).addOnSuccessListener {
            Toast.makeText(requireContext(), getString(R.string.download_success), Toast.LENGTH_SHORT)
                .show()
            detailViewModel.localPhoto = Uri.fromFile(localFile).toString()
            setPhoto()
        }.addOnFailureListener{
            Toast.makeText(requireContext(), getString(R.string.download_error), Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun setPhoto(){
        cameraButton.setImageURI(Uri.parse(detailViewModel.localPhoto))
        cameraButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fui_transparent))
    }

    override fun onImageSelect() {
        setPhoto()
    }
}
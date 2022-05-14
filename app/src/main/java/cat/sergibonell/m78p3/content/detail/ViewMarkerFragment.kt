package cat.sergibonell.m78p3.content.detail

import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import cat.sergibonell.m78p3.R
import cat.sergibonell.m78p3.databinding.FragmentAddMarkerBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ViewMarkerFragment: Fragment() {
    lateinit var binding: FragmentAddMarkerBinding
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

        doneButton.setOnClickListener { updateMarker() }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }

        if(detailViewModel.localPhoto != "")
            setPhoto()
        else if(detailViewModel.currentPhoto != "")
            getPhoto(detailViewModel.currentPhoto)


        titleText.setText(detailViewModel.currentTitle)
        descriptionText.setText(detailViewModel.currentDescription)
        val index = resources.getStringArray(R.array.category_list).indexOf(detailViewModel.currentCategory)
        categorySpinner.setSelection(index)

        titleText.inputType = InputType.TYPE_NULL
        descriptionText.inputType = InputType.TYPE_NULL
        categorySpinner.isEnabled = false
    }

    fun updateMarker(){
        findNavController().navigate(R.id.action_viewMarkerFragment_to_mapFragment)
    }

    fun getPhoto(uri: String){
        val storage = FirebaseStorage.getInstance().reference.child(uri)
        val localFile = File.createTempFile("temp", "")
        storage.getFile(localFile).addOnSuccessListener {
            Toast.makeText(requireContext(), getString(R.string.download_success), Toast.LENGTH_SHORT)
                .show()
            detailViewModel.localPhoto = localFile.absolutePath
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
}
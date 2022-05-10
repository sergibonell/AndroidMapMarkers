package cat.sergibonell.m78p3.content.detail

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import cat.sergibonell.m78p3.R
import cat.sergibonell.m78p3.content.map.MapViewModel
import cat.sergibonell.m78p3.data.PostData
import cat.sergibonell.m78p3.databinding.FragmentAddMarkerBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ViewMarkerFragment: Fragment() {
    lateinit var binding: FragmentAddMarkerBinding
    private val detailViewModel: DetailViewModel by activityViewModels()

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
        binding.button.setOnClickListener { updateMarker() }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }

        getPhoto(detailViewModel.currentPhoto)
        binding.titleText.setText(detailViewModel.currentTitle)
        binding.descriptionText.setText(detailViewModel.currentDescription)
        val index = resources.getStringArray(R.array.category_list).indexOf(detailViewModel.currentCategory)
        binding.spinner.setSelection(index)

        binding.titleText.inputType = InputType.TYPE_NULL
        binding.descriptionText.inputType = InputType.TYPE_NULL
        binding.spinner.isEnabled = false
    }

    fun updateMarker(){
        findNavController().navigate(R.id.action_viewMarkerFragment_to_mapFragment)
    }

    fun getPhoto(uri: String){
        val storage = FirebaseStorage.getInstance().reference.child(uri)
        val localFile = File.createTempFile("temp", "")
        storage.getFile(localFile).addOnSuccessListener {
            Toast.makeText(requireContext(), "Downloaded image!", Toast.LENGTH_SHORT)
                .show()
            detailViewModel.localPhoto = localFile.absolutePath
            binding.imageButton.setImageURI(Uri.parse(detailViewModel.localPhoto))
        }.addOnFailureListener{
            Toast.makeText(requireContext(), "Error downloading image!", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
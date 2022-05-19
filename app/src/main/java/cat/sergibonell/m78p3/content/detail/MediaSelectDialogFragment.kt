package cat.sergibonell.m78p3.content.detail

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import cat.sergibonell.m78p3.R
import cat.sergibonell.m78p3.databinding.FileSelectionDialogBinding

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import cat.sergibonell.m78p3.ImageSelectListener


class MediaSelectDialogFragment(listener: ImageSelectListener): DialogFragment() {
    lateinit var binding: FileSelectionDialogBinding
    lateinit var cameraButton: ImageButton
    lateinit var galleryButton: ImageButton

    private val detailViewModel: DetailViewModel by activityViewModels()
    private val selectPictureLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        detailViewModel.localPhoto = it.toString()
        dialog?.dismiss()
        listener.onImageSelect()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        binding = FileSelectionDialogBinding.inflate(layoutInflater)

        // Find views
        cameraButton = binding.camera
        galleryButton = binding.gallery

        cameraButton.setOnClickListener {
            if(parentFragment?.javaClass == EditMarkerFragment::class.java)
                findNavController().navigate(R.id.action_editMarkerFragment_to_cameraFragment)
            else
                findNavController().navigate(R.id.action_addMarkerFragment_to_cameraFragment)
        }

        galleryButton.setOnClickListener {
            selectPictureLauncher.launch("image/*")
        }

        // Build dialog
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

    companion object {
        const val TAG = "MediaSelectDialog"
    }
}
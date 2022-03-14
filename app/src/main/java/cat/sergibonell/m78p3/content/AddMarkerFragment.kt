package cat.sergibonell.m78p3.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cat.sergibonell.m78p3.R
import cat.sergibonell.m78p3.databinding.FragmentAddMarkerBinding

class AddMarkerFragment: Fragment() {
    lateinit var binding: FragmentAddMarkerBinding

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
    }

    fun openCamera(){
        findNavController().navigate(R.id.action_addMarkerFragment_to_cameraFragment)
    }
}
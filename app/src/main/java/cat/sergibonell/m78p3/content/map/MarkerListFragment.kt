package cat.sergibonell.m78p3.content.map

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CheckBox
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.sergibonell.m78p3.OnClickListener
import cat.sergibonell.m78p3.R
import cat.sergibonell.m78p3.content.detail.DetailViewModel
import cat.sergibonell.m78p3.data.PostData
import cat.sergibonell.m78p3.databinding.FragmentMarkerListBinding


class MarkerListFragment: Fragment(), OnClickListener {
    lateinit var binding: FragmentMarkerListBinding
    private val viewModel: MapViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var postList: ArrayList<PostData>
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarkerListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_markerListFragment_to_mapFragment)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        setupRecyclerView(arrayListOf())
        postList = arrayListOf<PostData>()
        observeMarkers()

        binding.tag1.setOnClickListener { onCheckboxClicked(it) }
        binding.tag2.setOnClickListener { onCheckboxClicked(it) }
        binding.tag3.setOnClickListener { onCheckboxClicked(it) }
    }

    private fun observeMarkers() {
        viewModel.postLiveData.observe(viewLifecycleOwner) {
            postAdapter.setUsersList(it as ArrayList<PostData>)
        }
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.clearAll()
    }

    override fun onClickDelete(post: PostData) {
        viewModel.deleteMarker(post)
    }

    override fun onClickEdit(post: PostData) {
        detailViewModel.setData(post)
        detailViewModel.edit = true

        findNavController().navigate(MarkerListFragmentDirections.actionMarkerListFragmentToEditMarkerFragment())
    }

    override fun onClickView(post: PostData) {
        detailViewModel.setData(post)

        findNavController().navigate(MarkerListFragmentDirections.actionMarkerListFragmentToViewMarkerFragment())
    }

    fun setupRecyclerView(posts: ArrayList<PostData>){
        postAdapter = PostAdapter(posts, this)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
        }
    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            var string = ""

            when (view.id) {
                R.id.tag1 -> string = "Landscape"
                R.id.tag2 -> string = "Monument"
                R.id.tag3 -> string = "People"
            }

            if (checked) {
                viewModel.categories.add(string)
            }
            else if(viewModel.categories.size > 1) {
                viewModel.categories.remove(string)
            }
            else{
                view.isChecked = true
            }
        }

        viewModel.getList()

        Log.d("CATEGORIES", viewModel.categories.toString())
    }
}
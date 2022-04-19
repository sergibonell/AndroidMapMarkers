package cat.sergibonell.m78p3.content

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.sergibonell.m78p3.OnClickListener
import cat.sergibonell.m78p3.R
import cat.sergibonell.m78p3.data.PostData
import cat.sergibonell.m78p3.databinding.FragmentMarkerListBinding
import com.google.firebase.firestore.*


class MarkerListFragment: Fragment(), OnClickListener {
    lateinit var binding: FragmentMarkerListBinding
    private val viewModel: MapViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var postList: ArrayList<PostData>
    private lateinit var postAdapter: PostAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarkerListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

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
        eventChangeListener()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.show_all -> {setupRecyclerView(viewModel.getList())}
            R.id.show_favs -> {setupRecyclerView(viewModel.getList("People"))}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClickDelete(post: PostData) {
        viewModel.deleteMarker(post)
    }

    override fun onClickEdit(post: PostData) {
        val action = MarkerListFragmentDirections.actionMarkerListFragmentToEditMarkerFragment(post)
        findNavController().navigate(action)
    }

    fun setupRecyclerView(posts: ArrayList<PostData>){
        postAdapter = PostAdapter(posts, this)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
        }
    }

    private fun eventChangeListener() {
        db.collection("markers").addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error != null){
                    Log.e("Firestore error", error.message.toString())
                    return
                }
                for(dc: DocumentChange in value?.documentChanges!!){
                    val newUser = dc.document.toObject(PostData::class.java)
                    newUser.id = dc.document.id

                    if(dc.type == DocumentChange.Type.ADDED)
                        postList.add(newUser)
                    else if(dc.type == DocumentChange.Type.REMOVED)
                        postList.remove(newUser)
                }
                postAdapter.setUsersList(postList)
            }
        })
    }


}
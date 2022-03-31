package cat.sergibonell.m78p3.content

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.sergibonell.m78p3.OnClickListener
import cat.sergibonell.m78p3.R
import cat.sergibonell.m78p3.data.PostData
import cat.sergibonell.m78p3.databinding.FragmentMarkerListBinding


class MarkerListFragment: Fragment(), OnClickListener {
    lateinit var binding: FragmentMarkerListBinding
    private val viewModel: MapViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        setupRecyclerView(viewModel.getList())
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
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun setupRecyclerView(posts: ArrayList<PostData>){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = PostAdapter(posts, this)
    }
}
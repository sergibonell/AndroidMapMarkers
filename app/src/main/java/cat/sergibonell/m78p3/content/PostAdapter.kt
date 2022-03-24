package cat.sergibonell.m78p3.content

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.sergibonell.m78p3.data.PostData
import cat.sergibonell.m78p3.databinding.PostViewBinding

class PostAdapter(postList: ArrayList<PostData>): RecyclerView.Adapter<PostAdapter.MainViewHolder>() {
    private val posts = postList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = PostViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindData(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class MainViewHolder(binding: PostViewBinding): RecyclerView.ViewHolder(binding.root) {
        private var titleTextView: TextView

        init {
            titleTextView = binding.title
        }

        fun bindData(postData: PostData){
            titleTextView.text = postData.title
        }
    }

}
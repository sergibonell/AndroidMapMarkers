package cat.sergibonell.m78p3.content

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.sergibonell.m78p3.OnClickListener
import cat.sergibonell.m78p3.data.PostData
import cat.sergibonell.m78p3.databinding.PostViewBinding

class PostAdapter(postList: ArrayList<PostData>, listener: OnClickListener): RecyclerView.Adapter<PostAdapter.MainViewHolder>() {
    private var posts = postList
    private val clickListener = listener

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

    fun setUsersList(list: ArrayList<PostData>){
        posts = list
        notifyDataSetChanged()
    }

    inner class MainViewHolder(binding: PostViewBinding): RecyclerView.ViewHolder(binding.root) {
        private var titleTextView: TextView
        private var deleteButton: ImageButton
        private var editButton: ImageButton

        init {
            titleTextView = binding.title
            deleteButton = binding.deleteButton
            editButton = binding.editButton
        }

        fun bindData(postData: PostData){
            titleTextView.text = postData.title
            deleteButton.setOnClickListener{
                clickListener.onClickDelete(postData)
            }
            editButton.setOnClickListener {
                clickListener.onClickEdit(postData)
            }
        }
    }

}
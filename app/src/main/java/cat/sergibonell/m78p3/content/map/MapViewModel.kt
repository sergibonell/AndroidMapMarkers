package cat.sergibonell.m78p3.content.map

import android.net.Uri
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.sergibonell.m78p3.content.detail.DetailViewModel
import cat.sergibonell.m78p3.data.PostData
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MapViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var categories = mutableListOf("Landscape", "Monument", "People")
    var sessionEmail = ""
    val postLiveData = MutableLiveData<List<PostData>>()

    fun addEventListener(){
        db.collection(sessionEmail).whereIn("category", categories).addSnapshotListener(object:
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error != null){
                    Log.e("Firestore error", error.message.toString())
                    return
                }
                getList()
            }
        })
    }

    fun getList(){
        db.collection(sessionEmail).whereIn("category", categories).get().addOnSuccessListener {
            val list = ArrayList<PostData>()

            for (document in it) {
                val newUser = document.toObject(PostData::class.java)
                newUser.id = document.id
                list.add(newUser)
            }

            postLiveData.value = list
        }
    }

    fun addMarker(marker: PostData) {
        if(marker.photoDirectory?.isNotEmpty()!!)
            marker.photoDirectory = uploadPicture(Uri.parse(marker.photoDirectory))

        db.collection(sessionEmail).add(
            hashMapOf("title" to marker.title,
                "description" to marker.description,
                "latitude" to marker.latitude,
                "longitude" to marker.longitude,
            "photoDirectory" to marker.photoDirectory,
            "category" to marker.category)
        )
    }

    fun editMarker(marker: PostData) {
        if(marker.photoDirectory?.isNotEmpty()!!)
            marker.photoDirectory = uploadPicture(Uri.parse(marker.photoDirectory))
        deletePicture(marker.id!!)

        db.collection(sessionEmail).document(marker.id!!).set(
            hashMapOf("title" to marker.title,
                "description" to marker.description,
                "latitude" to marker.latitude,
                "longitude" to marker.longitude,
                "photoDirectory" to marker.photoDirectory,
                "category" to marker.category)
        )
    }

    fun deleteMarker(marker: PostData) {
        deletePicture(marker.id!!)
        db.collection(sessionEmail).document(marker.id!!).delete()
    }

    fun getMarker(id: String): PostData {
        val task = db.collection(sessionEmail).document(id).get()

        while (!task.isSuccessful)
            continue

        val data = task.result.toObject(PostData::class.java)!!
        data.id = id

        return data
    }

    private fun uploadPicture(uri: Uri): String {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("images/$fileName")
        storage.putFile(uri)

        return storage.path
    }

    private fun deletePicture(id: String) {
        val path = getMarker(id).photoDirectory
        if(path?.isNotEmpty()!!) {
            val storage = FirebaseStorage.getInstance().reference.child(path)
            storage.delete()
        }
    }
}
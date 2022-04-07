package cat.sergibonell.m78p3.content

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.sergibonell.m78p3.data.PostData
import com.google.firebase.firestore.FirebaseFirestore

class MapViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var markerListLive: MutableLiveData<ArrayList<PostData>> = MutableLiveData()

    fun getList(category: String? = null): ArrayList<PostData>{
        var list = ArrayList<PostData>()

        db.collection("markers")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val newUser = document.toObject(PostData::class.java)
                    newUser.id = document.id
                    list.add(newUser)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("DOCUMENTS", "Error getting documents: ", exception)
            }

        return list
    }

    fun addMarker(marker: PostData) {
        db.collection("markers").add(
            hashMapOf("title" to marker.title,
                "description" to marker.description,
                "latitude" to marker.latitude,
                "longitude" to marker.longitude,
            "photoDirectory" to marker.photoDirectory,
            "category" to marker.category)
        )
    }

    fun deleteMarker(post: PostData) {
        val list = getList()
        list.remove(post)
        markerListLive.value = list
    }

    fun updateMarker(new: PostData, old: PostData) {
        val list = getList()
        list.remove(old)
        list.add(new)
        markerListLive.value = list
    }
}
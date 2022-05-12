package cat.sergibonell.m78p3.content.map

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.sergibonell.m78p3.data.PostData
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MapViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var categories = mutableListOf<String>("Landscape", "Monument", "People")
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
                val list = ArrayList<PostData>()
                for(dc: DocumentChange in value?.documentChanges!!){
                    val newUser = dc.document.toObject(PostData::class.java)
                    newUser.id = dc.document.id

                    if(dc.type == DocumentChange.Type.ADDED)
                        list.add(newUser)
                    else if(dc.type == DocumentChange.Type.REMOVED)
                        list.remove(newUser)
                }
                postLiveData.value = list
            }
        })
    }

    fun getList(){
        val list = ArrayList<PostData>()

        val task = db.collection(sessionEmail).whereIn("category", categories).get()

        while (!task.isSuccessful)
            continue

        for (document in task.result) {
            val newUser = document.toObject(PostData::class.java)
            newUser.id = document.id
            list.add(newUser)
        }

        Log.d("LIST", list.toString())
        postLiveData.value = list
    }

    fun addMarker(marker: PostData) {
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
        deletePicture(marker.id!!)
        marker.photoDirectory = uploadPicture(Uri.parse(marker.photoDirectory))

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
            .addOnSuccessListener {
                Log.d("PICTURE", "Uploaded")
            }
            .addOnFailureListener {
                Log.d("PICTURE", "Not Uploaded")
            }
        return storage.path
    }

    private fun deletePicture(id: String) {
        val path = getMarker(id).photoDirectory
        val storage = FirebaseStorage.getInstance().reference.child(path!!)

        storage.delete()
            .addOnSuccessListener {
                Log.d("PICTURE", "Deleted")
            }
            .addOnFailureListener {
                Log.d("PICTURE", "Not Deleted")
            }
    }
}
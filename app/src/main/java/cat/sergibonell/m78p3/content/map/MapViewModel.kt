package cat.sergibonell.m78p3.content.map

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import cat.sergibonell.m78p3.data.PostData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MapViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var sessionEmail = ""

    fun getList(category: String? = null): ArrayList<PostData>{
        var list = ArrayList<PostData>()

        db.collection(sessionEmail)
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
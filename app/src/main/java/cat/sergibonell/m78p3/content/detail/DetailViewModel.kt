package cat.sergibonell.m78p3.content.detail

import android.os.Debug
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.type.LatLng

class DetailViewModel: ViewModel() {
    var edit = false
    var currentId = ""
    var currentLatitude = 0.0
    var currentLongitude = 0.0
    var currentTitle = ""
    var currentDescription = ""
    var currentCategory = ""
    var currentPhoto = ""
    var localPhoto = ""

    fun clearAll(){
        edit = false
        currentId = ""
        currentLatitude = 0.0
        currentLongitude = 0.0
        currentTitle = ""
        currentDescription = ""
        currentCategory = ""
        currentPhoto = ""
        localPhoto = ""
    }

    fun setData(id: String, latitude: Double, longitude: Double, title: String, description: String, category: String, photo: String){
        currentId = id
        currentLatitude = latitude
        currentLongitude = longitude
        currentTitle = title
        currentDescription = description
        currentCategory = category
        currentPhoto = photo
    }

    fun printAll(){
        Log.d("MODELVIEW", "$currentId $currentLatitude $currentLongitude $currentTitle $currentDescription $currentCategory $currentPhoto")
    }
}
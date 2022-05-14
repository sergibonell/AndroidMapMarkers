package cat.sergibonell.m78p3.content.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import cat.sergibonell.m78p3.data.PostData

class DetailViewModel: ViewModel() {
    var edit = false
    var localPhoto = ""

    var currentId = ""
    var currentLatitude = 0.0
    var currentLongitude = 0.0
    var currentTitle = ""
    var currentDescription = ""
    var currentCategory = ""
    var currentPhoto = ""

    fun clearAll(){
        edit = false
        localPhoto = ""
        currentId = ""
        currentLatitude = 0.0
        currentLongitude = 0.0
        currentTitle = ""
        currentDescription = ""
        currentCategory = ""
        currentPhoto = ""
    }

    fun setData(data: PostData){
        currentId = data.id!!
        currentLatitude = data.latitude!!
        currentLongitude = data.longitude!!
        currentTitle = data.title!!
        currentDescription = data.description!!
        currentCategory = data.category!!
        currentPhoto = data.photoDirectory!!
    }

    fun printAll(){
        Log.d("MODELVIEW", "$currentId $currentLatitude $currentLongitude $currentTitle $currentDescription $currentCategory $currentPhoto")
    }
}
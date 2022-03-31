package cat.sergibonell.m78p3.content

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.sergibonell.m78p3.data.PostData

class MapViewModel: ViewModel() {
    var markerListLive: MutableLiveData<ArrayList<PostData>> = MutableLiveData()

    fun getList(category: String? = null): ArrayList<PostData>{
        var list = ArrayList<PostData>()

        if(markerListLive.value != null) {
            list = if(category != null)
                markerListLive.value!!.filter { it.category == category } as ArrayList<PostData>
            else
                markerListLive.value!!
        }

        return list
    }

    fun addMarkerList(marker: PostData) {
        val list = getList()
        list.add(marker)
        markerListLive.value = list
    }

    fun deleteMarker(post: PostData) {
        val list = getList()
        list.remove(post)
        markerListLive.value = list
    }

}
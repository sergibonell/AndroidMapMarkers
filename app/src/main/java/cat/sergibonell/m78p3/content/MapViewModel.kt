package cat.sergibonell.m78p3.content

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.sergibonell.m78p3.data.PostData

class MapViewModel: ViewModel() {
    var markerListLive: MutableLiveData<ArrayList<PostData>> = MutableLiveData()

    fun addMarkerList(marker: PostData) {
        val list = getList()
        list.add(marker)
        markerListLive.value = list
    }

    fun getList(): ArrayList<PostData>{
        var list = ArrayList<PostData>()

        if(markerListLive.value != null)
            list = markerListLive.value!!

        return list
    }
}
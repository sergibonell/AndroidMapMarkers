package cat.sergibonell.m78p3.content

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.MarkerOptions

class MapViewModel: ViewModel() {
    var markerList = arrayListOf<MarkerOptions>()
}
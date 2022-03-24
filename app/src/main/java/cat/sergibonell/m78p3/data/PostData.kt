package cat.sergibonell.m78p3.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostData(val title: String, val description: String, val photoDirectory: String, val position: LatLng): Parcelable

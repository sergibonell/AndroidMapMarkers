package cat.sergibonell.m78p3.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostData(var id: String?="", var title: String?="", val description: String?="", val category: String?="", val photoDirectory: String?="", val latitude: Double?=0.0, val longitude: Double?=0.0): Parcelable

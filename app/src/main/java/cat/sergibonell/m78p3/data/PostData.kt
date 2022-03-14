package cat.sergibonell.m78p3.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostData(val title: String, val description: String, val photoDirectory: String): Parcelable

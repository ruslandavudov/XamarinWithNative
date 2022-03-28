package com.example.nativelib.models.api.streaming

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Avatar(

    @SerializedName("src") var src: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("size") var size: String? = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(src)
        parcel.writeString(type)
        parcel.writeString(size)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Avatar> {
        override fun createFromParcel(parcel: Parcel): Avatar {
            return Avatar(parcel)
        }

        override fun newArray(size: Int): Array<Avatar?> {
            return arrayOfNulls(size)
        }
    }
}
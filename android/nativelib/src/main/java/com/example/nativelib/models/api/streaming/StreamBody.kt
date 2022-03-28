package com.example.nativelib.models.api.streaming

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class StreamBody(

    @SerializedName("streamId") val streamId: String?,
    @SerializedName("streamStatus") val streamStatus: String?,
    @SerializedName("productId") val productId: String?,
    @SerializedName("playbackLink") val playbackLink: String?,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(streamId)
        parcel.writeString(streamStatus)
        parcel.writeString(productId)
        parcel.writeString(playbackLink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StreamBody> {
        override fun createFromParcel(parcel: Parcel): StreamBody {
            return StreamBody(parcel)
        }

        override fun newArray(size: Int): Array<StreamBody?> {
            return arrayOfNulls(size)
        }
    }
}
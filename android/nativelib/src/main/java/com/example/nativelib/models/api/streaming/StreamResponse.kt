package com.example.nativelib.models.api.streaming

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class StreamResponse(

    @SerializedName("status") override val status: Int?,
    @SerializedName("body") val body: StreamBody?,
    @SerializedName("serverTime") override val serverTime: String?,

    ) : ResponseBase, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(StreamBody::class.java.classLoader),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(status)
        parcel.writeParcelable(body, flags)
        parcel.writeString(serverTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StreamResponse> {
        override fun createFromParcel(parcel: Parcel): StreamResponse {
            return StreamResponse(parcel)
        }

        override fun newArray(size: Int): Array<StreamResponse?> {
            return arrayOfNulls(size)
        }
    }
}
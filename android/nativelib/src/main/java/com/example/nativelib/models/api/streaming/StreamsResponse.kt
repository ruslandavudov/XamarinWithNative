package com.example.nativelib.models.api.streaming

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class StreamsResponse(

    @SerializedName("status") override val status: Int?,
    @SerializedName("body") val body: StreamsBody?,
    @SerializedName("serverTime") override val serverTime: String?,

    ) : ResponseBase, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(StreamsBody::class.java.classLoader),
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

    companion object CREATOR : Parcelable.Creator<StreamsResponse> {
        override fun createFromParcel(parcel: Parcel): StreamsResponse {
            return StreamsResponse(parcel)
        }

        override fun newArray(size: Int): Array<StreamsResponse?> {
            return arrayOfNulls(size)
        }
    }
}
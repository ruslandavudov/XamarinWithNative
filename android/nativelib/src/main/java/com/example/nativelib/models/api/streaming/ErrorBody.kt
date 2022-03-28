package com.example.nativelib.models.api.streaming

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ErrorBody(
    @SerializedName("error") val error: String? = null,
    @SerializedName("message") val message: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(error)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ErrorBody> {
        override fun createFromParcel(parcel: Parcel): ErrorBody {
            return ErrorBody(parcel)
        }

        override fun newArray(size: Int): Array<ErrorBody?> {
            return arrayOfNulls(size)
        }
    }
}
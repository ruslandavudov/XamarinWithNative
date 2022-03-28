package com.example.nativelib.models.api.streaming

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Streamers(

    @SerializedName("id") var id: String? = null,
    @SerializedName("application") var application: String? = null,
    @SerializedName("profile") var profile: Profile? = Profile()

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Profile::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(application)
        parcel.writeParcelable(profile, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Streamers> {
        override fun createFromParcel(parcel: Parcel): Streamers {
            return Streamers(parcel)
        }

        override fun newArray(size: Int): Array<Streamers?> {
            return arrayOfNulls(size)
        }
    }
}
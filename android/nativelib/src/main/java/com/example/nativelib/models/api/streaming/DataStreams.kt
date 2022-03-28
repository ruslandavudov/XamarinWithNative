package com.example.nativelib.models.api.streaming

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class DataStreams(
    @SerializedName("id") val id: String? = null,
    @SerializedName("application") val application: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("streamer") val streamer: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("plannedDate") val plannedDate: String? = null,
    @SerializedName("startDate") val startDate: String? = null,
    @SerializedName("endDate") val endDate: String? = null,
    @SerializedName("previewImages") val previewImages: ArrayList<PreviewImages> = arrayListOf(),
    @SerializedName("products") val products: ArrayList<String> = arrayListOf(),
    @SerializedName("categories") val categories: ArrayList<String> = arrayListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        arrayListOf<PreviewImages>().apply {
            parcel.readList(this, PreviewImages::class.java.classLoader)
        },
        arrayListOf<String>().apply {
            parcel.readList(this, String::class.java.classLoader)
        },
        arrayListOf<String>().apply {
            parcel.readList(this, String::class.java.classLoader)
        },
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(application)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(streamer)
        parcel.writeString(status)
        parcel.writeString(plannedDate)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeList(previewImages)
        parcel.writeList(products)
        parcel.writeList(categories)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataStreams> {
        override fun createFromParcel(parcel: Parcel): DataStreams {
            return DataStreams(parcel)
        }

        override fun newArray(size: Int): Array<DataStreams?> {
            return arrayOfNulls(size)
        }
    }
}
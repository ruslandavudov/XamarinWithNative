package com.example.nativelib.models.api.streaming

import android.os.Parcel
import android.os.Parcelable
import com.example.nativelib.models.api.streaming.PreviewImages
import com.google.gson.annotations.SerializedName


data class Products(

    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("application") var application: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("previewImages") var previewImages: ArrayList<PreviewImages> = arrayListOf()

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        arrayListOf<PreviewImages>().apply {
            parcel.readList(this, PreviewImages::class.java.classLoader)
        },
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(application)
        parcel.writeString(url)
        parcel.writeList(previewImages)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Products> {
        override fun createFromParcel(parcel: Parcel): Products {
            return Products(parcel)
        }

        override fun newArray(size: Int): Array<Products?> {
            return arrayOfNulls(size)
        }
    }
}
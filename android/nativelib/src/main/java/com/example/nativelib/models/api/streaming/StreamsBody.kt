package com.example.nativelib.models.api.streaming

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class StreamsBody(

    @SerializedName("plannedStreams") var plannedStreams: ArrayList<DataStreams>? = arrayListOf(),
    @SerializedName("availableStreams") var availableStreams: ArrayList<DataStreams>? = arrayListOf(),
    @SerializedName("products") var products: ArrayList<Products>? = arrayListOf(),
    @SerializedName("streamers") var streamers: ArrayList<Streamers>? = arrayListOf(),
    @SerializedName("categories") var categories: ArrayList<Categories>? = arrayListOf()

) : Parcelable {
    constructor(parcel: Parcel) : this(
        arrayListOf<DataStreams>().apply {
            parcel.readList(this, DataStreams::class.java.classLoader)
        },
        arrayListOf<DataStreams>().apply {
            parcel.readList(this, DataStreams::class.java.classLoader)
        },
        arrayListOf<Products>().apply {
            parcel.readList(this, Products::class.java.classLoader)
        },
        arrayListOf<Streamers>().apply {
            parcel.readList(this, Streamers::class.java.classLoader)
        },
        arrayListOf<Categories>().apply {
            parcel.readList(this, Categories::class.java.classLoader)
        },
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(plannedStreams)
        parcel.writeList(availableStreams)
        parcel.writeList(products)
        parcel.writeList(streamers)
        parcel.writeList(categories)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StreamsBody> {
        override fun createFromParcel(parcel: Parcel): StreamsBody {
            return StreamsBody(parcel)
        }

        override fun newArray(size: Int): Array<StreamsBody?> {
            return arrayOfNulls(size)
        }
    }
}
package com.example.nativelib.models.api.streaming

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Product(

    @SerializedName("name") val name: String? = null,
    @SerializedName("feedProductId") val feedProductId: String? = null,
    @SerializedName("feedProductGroupId") val feedProductGroupId: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("vendorCode") val vendorCode: String? = null,

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(feedProductId)
        parcel.writeString(feedProductGroupId)
        parcel.writeString(url)
        parcel.writeString(vendorCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
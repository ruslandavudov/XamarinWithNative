package com.example.nativelib.models.api.streaming

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class PreviewImages (

  @SerializedName("src"     ) var src     : String? = null,
  @SerializedName("mediaId" ) var mediaId : String? = null

) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readString()
  )

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(src)
    parcel.writeString(mediaId)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<PreviewImages> {
    override fun createFromParcel(parcel: Parcel): PreviewImages {
      return PreviewImages(parcel)
    }

    override fun newArray(size: Int): Array<PreviewImages?> {
      return arrayOfNulls(size)
    }
  }
}
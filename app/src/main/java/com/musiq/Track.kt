package com.musiq

import android.os.Parcel
import android.os.Parcelable

class Track() : Parcelable{

    var name : String = "UNKNOWN TRACK"
    var num : String = "0"
    var duration: String = "0"
    var location: String = "0"

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()!!
        num = parcel.readString()!!
        duration = parcel.readString()!!
        location = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(num)
        parcel.writeString(duration)
        parcel.writeString(location)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }

}
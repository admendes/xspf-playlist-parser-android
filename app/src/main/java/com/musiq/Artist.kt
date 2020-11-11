package com.musiq

import android.os.Parcel
import android.os.Parcelable

class Artist() : Parcelable{

    var name : String = "UNKNOWN ARTIST"
    var albumList : ArrayList<Album>? = ArrayList()

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()!!
        parcel.readTypedList(albumList, Album.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeTypedList(albumList);
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Artist> {
        override fun createFromParcel(parcel: Parcel): Artist {
            return Artist(parcel)
        }

        override fun newArray(size: Int): Array<Artist?> {
            return arrayOfNulls(size)
        }
    }
}
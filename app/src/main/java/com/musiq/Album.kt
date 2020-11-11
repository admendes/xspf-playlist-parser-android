package com.musiq

import android.os.Parcel
import android.os.Parcelable

class Album() : Parcelable {

    var name : String = "UNKNOWN ALBUM"
    var trackList : ArrayList<Track> = ArrayList()
    //var trackMap: LinkedHashMap<String, Track> = LinkedHashMap()

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()!!
        parcel.readTypedList(trackList, Track.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeTypedList(trackList);
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Album> {
        override fun createFromParcel(parcel: Parcel): Album {
            return Album(parcel)
        }

        override fun newArray(size: Int): Array<Album?> {
            return arrayOfNulls(size)
        }
    }

}
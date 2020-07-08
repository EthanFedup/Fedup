package com.evertech.Fedup.login.model

import android.os.Parcel
import android.os.Parcelable

class Country() :Parcelable{
    var mWord: String=""
    var name: String=""
    var number: String=""
    var pid: Int = 0

    constructor(parcel: Parcel) : this() {
        mWord = parcel.readString().toString()
        name = parcel.readString().toString()
        number = parcel.readString().toString()
        pid = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mWord)
        parcel.writeString(name)
        parcel.writeString(number)
        parcel.writeInt(pid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Country> {
        override fun createFromParcel(parcel: Parcel): Country {
            return Country(parcel)
        }

        override fun newArray(size: Int): Array<Country?> {
            return arrayOfNulls(size)
        }
    }


}
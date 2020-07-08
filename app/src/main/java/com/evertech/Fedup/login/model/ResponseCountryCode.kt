package com.evertech.Fedup.login.model

import android.os.Parcel
import android.os.Parcelable

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/7/2020 12:01 PM
 *    desc   :
 */
class ResponseCountryCode() : Parcelable {

    val code: Int = 0
    val `data`: MutableList<WrapCountryData> = ArrayList()
    val status: String = ""

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseCountryCode> {
        override fun createFromParcel(parcel: Parcel): ResponseCountryCode {
            return ResponseCountryCode(parcel)
        }

        override fun newArray(size: Int): Array<ResponseCountryCode?> {
            return arrayOfNulls(size)
        }
    }
}

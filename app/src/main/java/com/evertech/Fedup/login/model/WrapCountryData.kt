package com.evertech.Fedup.login.model

import android.os.Parcel
import android.os.Parcelable

 class WrapCountryData() : Parcelable {

    val countrys: List<Country> = ArrayList()
    val id: Int = 0
    val name: String = ""

     constructor(parcel: Parcel) : this() {
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {

     }

     override fun describeContents(): Int {
         return 0
     }

     companion object CREATOR : Parcelable.Creator<WrapCountryData> {
         override fun createFromParcel(parcel: Parcel): WrapCountryData {
             return WrapCountryData(parcel)
         }

         override fun newArray(size: Int): Array<WrapCountryData?> {
             return arrayOfNulls(size)
         }
     }
 }

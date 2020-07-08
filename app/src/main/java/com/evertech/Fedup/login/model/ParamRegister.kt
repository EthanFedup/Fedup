package com.evertech.Fedup.login.model

import android.os.Parcel
import android.os.Parcelable

/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/6/2020 1:49 PM
 *    desc   : 注册需要的参数
 */
class ParamRegister() :Parcelable{
    var phone = ""
    var ident_code = ""
    var email = ""
    var password = ""
    var area_code = ""
    var name = ""
    var deactivation = false
    var appleIdent = ""

    constructor(parcel: Parcel) : this() {
        phone = parcel.readString().toString()
        ident_code = parcel.readString().toString()
        email = parcel.readString().toString()
        password = parcel.readString().toString()
        area_code = parcel.readString().toString()
        name = parcel.readString().toString()
        deactivation = parcel.readByte() != 0.toByte()
        appleIdent = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(phone)
        parcel.writeString(ident_code)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(area_code)
        parcel.writeString(name)
        parcel.writeByte(if (deactivation) 1 else 0)
        parcel.writeString(appleIdent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParamRegister> {
        override fun createFromParcel(parcel: Parcel): ParamRegister {
            return ParamRegister(parcel)
        }

        override fun newArray(size: Int): Array<ParamRegister?> {
            return arrayOfNulls(size)
        }
    }

}
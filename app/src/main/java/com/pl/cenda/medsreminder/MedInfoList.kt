package com.pl.cenda.medsreminder

import android.os.Parcel
import android.os.Parcelable

class MedInfoList(val name: String, val medDetails: ArrayList<String> = ArrayList()): Parcelable {

    constructor(parcel: Parcel): this(
        parcel.readString()!!,
        parcel.createStringArrayList()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeStringList(medDetails)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MedInfoList> {
        override fun createFromParcel(parcel: Parcel): MedInfoList {
            return MedInfoList(parcel)
        }

        override fun newArray(size: Int): Array<MedInfoList?> {
            return arrayOfNulls(size)
        }
    }
}
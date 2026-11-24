package com.example.wisatara.model

import android.os.Parcel
import android.os.Parcelable

data class PaketTurModel(
    var idPaket: String = "",
    var judulTur: String = "",
    var namaPemandu: String = "",
    var namaPanggilan: String = "",
    var taglinePemandu: String = "",
    var kotaDestinasi: String = "",
    var kategoriTur: String = "",
    var bahasaPemandu: String = "",
    var durasiJam: Int = 0,
    var kuotaMaksimal: Int = 0,
    var hargaPerOrang: Int = 0,
    var titikKumpul: String = "",
    var deskripsiTur: String = "",
    var rating: Double = 4.9,
    var jumlahUlasan: Int = 124,
    var highlightTag: String = "Maks. 6 Orang"
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idPaket)
        parcel.writeString(judulTur)
        parcel.writeString(namaPemandu)
        parcel.writeString(namaPanggilan)
        parcel.writeString(taglinePemandu)
        parcel.writeString(kotaDestinasi)
        parcel.writeString(kategoriTur)
        parcel.writeString(bahasaPemandu)
        parcel.writeInt(durasiJam)
        parcel.writeInt(kuotaMaksimal)
        parcel.writeInt(hargaPerOrang)
        parcel.writeString(titikKumpul)
        parcel.writeString(deskripsiTur)
        parcel.writeDouble(rating)
        parcel.writeInt(jumlahUlasan)
        parcel.writeString(highlightTag)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<PaketTurModel> {
        override fun createFromParcel(parcel: Parcel): PaketTurModel {
            return PaketTurModel(parcel)
        }

        override fun newArray(size: Int): Array<PaketTurModel?> {
            return arrayOfNulls(size)
        }
    }
}

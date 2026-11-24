package com.example.wisatara.model

import android.os.Parcel
import android.os.Parcelable

data class PesananModel(
    var idPesanan: String = "",
    var idPaket: String = "",
    var judulTur: String = "",
    var namaPemandu: String = "",
    var kotaDestinasi: String = "",
    var tanggalTur: String = "",
    var jumlahTamu: Int = 1,
    var totalHarga: Int = 0,
    var statusPesanan: String = "Aktif", // "Aktif", "Selesai", "Dibatalkan"
    var titikKumpul: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "Aktif",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idPesanan)
        parcel.writeString(idPaket)
        parcel.writeString(judulTur)
        parcel.writeString(namaPemandu)
        parcel.writeString(kotaDestinasi)
        parcel.writeString(tanggalTur)
        parcel.writeInt(jumlahTamu)
        parcel.writeInt(totalHarga)
        parcel.writeString(statusPesanan)
        parcel.writeString(titikKumpul)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<PesananModel> {
        override fun createFromParcel(parcel: Parcel): PesananModel {
            return PesananModel(parcel)
        }

        override fun newArray(size: Int): Array<PesananModel?> {
            return arrayOfNulls(size)
        }
    }
}

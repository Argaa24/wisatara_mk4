package com.example.wisatara.repository

import com.example.wisatara.model.PesananModel

object PesananRepository {

    private val orderList: MutableList<PesananModel> = mutableListOf(
        PesananModel(
            idPesanan = "#BOOK-2024-001",
            idPaket = "#WST-2024-001",
            judulTur = "The 10 Tastings Street Food Jogja",
            namaPemandu = "Budi Prasetyo",
            kotaDestinasi = "Yogyakarta",
            tanggalTur = "15 Des 2024",
            jumlahTamu = 2,
            totalHarga = 700000,
            statusPesanan = "Aktif",
            titikKumpul = "Lobby Grand Inna Malioboro, Jl. Malioboro No. 60"
        ),
        PesananModel(
            idPesanan = "#BOOK-2024-002",
            idPaket = "#WST-2024-003",
            judulTur = "Old Batavia Heritage Walk & Secret Coffee",
            namaPemandu = "Siti Nurhaliza",
            kotaDestinasi = "Jakarta",
            tanggalTur = "28 Nov 2024",
            jumlahTamu = 1,
            totalHarga = 275000,
            statusPesanan = "Selesai",
            titikKumpul = "Museum Fatahillah Kota Tua Jakarta"
        ),
        PesananModel(
            idPesanan = "#BOOK-2024-003",
            idPaket = "#WST-2024-002",
            judulTur = "Ubud Hidden Waterfalls & Traditional Cooking",
            namaPemandu = "Wayan Suartana",
            kotaDestinasi = "Bali",
            tanggalTur = "10 Okt 2024",
            jumlahTamu = 2,
            totalHarga = 960000,
            statusPesanan = "Selesai",
            titikKumpul = "Ubud Center, Jl. Raya Ubud No. 1"
        )
    )

    fun getAll(): MutableList<PesananModel> {
        return orderList
    }

    fun getByStatus(status: String): List<PesananModel> {
        if (status.equals("Semua", ignoreCase = true)) {
            return orderList
        }
        return orderList.filter { it.statusPesanan.equals(status, ignoreCase = true) }
    }

    fun addPesanan(item: PesananModel) {
        orderList.add(0, item)
    }

    fun batalkanPesanan(idPesanan: String): Boolean {
        val pesanan = orderList.find { it.idPesanan == idPesanan }
        if (pesanan != null) {
            pesanan.statusPesanan = "Dibatalkan"
            return true
        }
        return false
    }

    fun getJumlahSelesai(): Int {
        return orderList.count { it.statusPesanan.equals("Selesai", ignoreCase = true) }
    }
}

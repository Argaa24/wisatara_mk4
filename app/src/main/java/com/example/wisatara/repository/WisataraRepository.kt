package com.example.wisatara.repository

import com.example.wisatara.model.PaketTurModel

object WisataraRepository {
    private val dataList: MutableList<PaketTurModel> = mutableListOf(
        PaketTurModel(
            idPaket = "#WST-2024-001",
            judulTur = "The 10 Tastings Street Food Jogja",
            namaPemandu = "Budi Prasetyo",
            namaPanggilan = "Budi - The Food Lover",
            taglinePemandu = "Pecinta kuliner tradisional & pemandu berlisensi sejak 2018",
            kotaDestinasi = "Yogyakarta",
            kategoriTur = "Food & Drink",
            bahasaPemandu = "English, Indonesia",
            durasiJam = 3,
            kuotaMaksimal = 6,
            hargaPerOrang = 350000,
            titikKumpul = "Lobby Grand Inna Malioboro, Jl. Malioboro No. 60",
            deskripsiTur = "Menjelajahi 10 kelezatan kuliner jalanan khas Yogyakarta bersama pemandu lokal berlisensi. Menikmati Gudeg Yu Djum, Pasar Kranggan hidden food court, Kopi Joss, dan wedang ronde hangat sambil menelusuri sejarah gang heritage Malioboro.",
            rating = 4.9,
            jumlahUlasan = 124,
            highlightTag = "Maks. 6 Orang"
        ),
        PaketTurModel(
            idPaket = "#WST-2024-002",
            judulTur = "Ubud Hidden Waterfalls & Traditional Cooking",
            namaPemandu = "Wayan Suartana",
            namaPanggilan = "Wayan - Culture Enthusiast",
            taglinePemandu = "Pemandu budaya & alam Bali berpengalaman 7 tahun",
            kotaDestinasi = "Bali",
            kategoriTur = "Nature & Spiritual",
            bahasaPemandu = "English",
            durasiJam = 5,
            kuotaMaksimal = 8,
            hargaPerOrang = 480000,
            titikKumpul = "Ubud Center, Jl. Raya Ubud No. 1",
            deskripsiTur = "Menelusuri air terjun tersembunyi di sekitar Ubud dan belajar memasak hidangan tradisional Bali bersama keluarga lokal di rumah panggung khas Bali.",
            rating = 4.95,
            jumlahUlasan = 89,
            highlightTag = "Termasuk Makan Siang"
        ),
        PaketTurModel(
            idPaket = "#WST-2024-003",
            judulTur = "Old Batavia Heritage Walk & Secret Coffee Tasting",
            namaPemandu = "Siti Nurhaliza",
            namaPanggilan = "Siti - Jakarta Historian",
            taglinePemandu = "Peneliti sejarah Kota Tua & penikmat kopi lokal",
            kotaDestinasi = "Jakarta",
            kategoriTur = "History & Culture",
            bahasaPemandu = "ID, EN",
            durasiJam = 3,
            kuotaMaksimal = 10,
            hargaPerOrang = 275000,
            titikKumpul = "Museum Fatahillah Kota Tua Jakarta",
            deskripsiTur = "Menelusuri jejak arsitektur kolonial Batavia tempo doeloe, mengunjungi warkop legendaris berusia 100 tahun, dan mencicipi racikan kopi rahasia para bangsawan Batavia.",
            rating = 4.88,
            jumlahUlasan = 67,
            highlightTag = "Coffee Tasting"
        ),
        PaketTurModel(
            idPaket = "#WST-2024-004",
            judulTur = "Mount Bromo Sunrise & Tenggerese Village Life",
            namaPemandu = "Agus Setiawan",
            namaPanggilan = "Agus - Volcano Trekker",
            taglinePemandu = "Pengembara Bromo & putra suku Tengger",
            kotaDestinasi = "Malang, Jawa Timur",
            kategoriTur = "Nature Walk",
            bahasaPemandu = "ID, EN",
            durasiJam = 8,
            kuotaMaksimal = 6,
            hargaPerOrang = 650000,
            titikKumpul = "Stasiun Malang Kotabaru",
            deskripsiTur = "Menikmati keindahan sunrise spektakuler di Penanjakan Bromo dengan Jeep 4WD, menjelajahi lautan pasir, dan berinteraksi langsung dengan kehidupan warga desa suku Tengger.",
            rating = 4.98,
            jumlahUlasan = 142,
            highlightTag = "Jeep 4WD Included"
        )
    )

    fun getAll(): MutableList<PaketTurModel> {
        return dataList
    }

    fun getById(id: String): PaketTurModel? {
        return dataList.find { it.idPaket == id }
    }

    fun add(item: PaketTurModel) {
        dataList.add(0, item)
    }

    fun update(item: PaketTurModel) {
        val index = dataList.indexOfFirst { it.idPaket == item.idPaket }
        if (index != -1) {
            dataList[index] = item
        }
    }

    fun deleteById(idPaket: String) {
        dataList.removeAll { it.idPaket == idPaket }
    }
}

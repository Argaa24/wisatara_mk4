package com.example.wisatara.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wisatara.R
import com.example.wisatara.databinding.ItemPesananBinding
import com.example.wisatara.model.PesananModel
import java.text.NumberFormat
import java.util.Locale

class PesananAdapter(
    private var listPesanan: MutableList<PesananModel>,
    private val onItemClick: (PesananModel) -> Unit,
    private val onCancelClick: (PesananModel) -> Unit,
    private val onContactClick: (PesananModel) -> Unit
) : RecyclerView.Adapter<PesananAdapter.PesananViewHolder>() {

    inner class PesananViewHolder(val binding: ItemPesananBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesananViewHolder {
        val binding = ItemPesananBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PesananViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PesananViewHolder, position: Int) {
        val item = listPesanan[position]
        val context = holder.itemView.context
        val formatRp = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val formattedHarga = formatRp.format(item.totalHarga).replace(",00", "")

        with(holder.binding) {
            tvIdBooking.text = item.idPesanan
            tvJudulTurPesanan.text = item.judulTur
            tvHostDanKota.text = "Pemandu: ${item.namaPemandu} • ${item.kotaDestinasi}"
            tvTanggalDanTamu.text = "Tanggal: ${item.tanggalTur} • ${item.jumlahTamu} Tamu"
            tvTitikKumpulPesanan.text = "Titik: ${item.titikKumpul}"
            tvTotalHargaPesanan.text = formattedHarga

            // Status Badge & Action buttons logic
            when (item.statusPesanan) {
                "Aktif" -> {
                    tvStatusBadge.text = "Aktif"
                    tvStatusBadge.background = ContextCompat.getDrawable(context, R.drawable.bg_pill_mint)
                    tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.forest_green_dark))
                    btnBatalkanPesanan.visibility = View.VISIBLE
                }
                "Selesai" -> {
                    tvStatusBadge.text = "Selesai"
                    tvStatusBadge.background = ContextCompat.getDrawable(context, R.drawable.bg_pill_emerald)
                    tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.white))
                    btnBatalkanPesanan.visibility = View.GONE
                }
                "Dibatalkan" -> {
                    tvStatusBadge.text = "Dibatalkan"
                    tvStatusBadge.background = ContextCompat.getDrawable(context, R.drawable.bg_button_red_soft)
                    tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.danger_red))
                    btnBatalkanPesanan.visibility = View.GONE
                }
                else -> {
                    tvStatusBadge.text = item.statusPesanan
                    tvStatusBadge.background = ContextCompat.getDrawable(context, R.drawable.bg_pill_white)
                    tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.text_dark))
                    btnBatalkanPesanan.visibility = View.GONE
                }
            }

            root.setOnClickListener {
                onItemClick(item)
            }

            btnBatalkanPesanan.setOnClickListener {
                onCancelClick(item)
            }

            btnHubungiHost.setOnClickListener {
                onContactClick(item)
            }
        }
    }

    override fun getItemCount(): Int = listPesanan.size

    fun updateData(newList: List<PesananModel>) {
        listPesanan.clear()
        listPesanan.addAll(newList)
        notifyDataSetChanged()
    }
}

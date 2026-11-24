package com.example.wisatara.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wisatara.databinding.ItemRowBinding
import com.example.wisatara.model.PaketTurModel
import java.text.NumberFormat
import java.util.Locale

class PaketTurAdapter(
    private var listPaket: MutableList<PaketTurModel>,
    private val canManageCrud: Boolean = true,
    private val onItemClick: (PaketTurModel) -> Unit,
    private val onEditClick: (PaketTurModel) -> Unit
) : RecyclerView.Adapter<PaketTurAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPaket[position]
        val formatRp = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val formattedHarga = formatRp.format(item.hargaPerOrang).replace(",00", "")

        holder.binding.tvJudulTur.text = item.judulTur
        holder.binding.tvNamaPemandu.text = "${item.namaPanggilan} ✔️ • Bersertifikat HPI"
        holder.binding.tvDurasiPill.text = "🕒 ${item.durasiJam} Jam"
        holder.binding.tvKuotaPill.text = "👥 ${item.highlightTag}"
        holder.binding.tvBahasaPill.text = "文A ${item.bahasaPemandu}"
        holder.binding.tvHargaPerOrang.text = "$formattedHarga / orang"
        holder.binding.tvRatingBadge.text = "★ ${item.rating} (${item.jumlahUlasan})"
        holder.binding.tvCategoryBanner.text = item.kategoriTur.uppercase()

        holder.binding.root.setOnClickListener { onItemClick(item) }
        holder.binding.btnDetail.setOnClickListener { onItemClick(item) }
        holder.binding.btnEditShortcut.visibility = if (canManageCrud) View.VISIBLE else View.GONE
        holder.binding.btnEditShortcut.setOnClickListener {
            if (canManageCrud) onEditClick(item)
        }
    }

    override fun getItemCount(): Int = listPaket.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<PaketTurModel>) {
        listPaket.clear()
        listPaket.addAll(newList)
        notifyDataSetChanged()
    }
}

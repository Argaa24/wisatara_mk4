package com.example.wisatara

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wisatara.adapter.PaketTurAdapter
import com.example.wisatara.databinding.ActivityListDataBinding
import com.example.wisatara.repository.WisataraRepository

class ListDataActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_QUERY = "EXTRA_QUERY"
        const val EXTRA_FOCUS_SEARCH = "EXTRA_FOCUS_SEARCH"
    }

    private enum class CategoryFilter { ALL, FOOD, CULTURE }
    private enum class SortMode(val label: String) {
        DEFAULT("Urutkan ▼"),
        PRICE_ASC("Harga Terendah ▼"),
        PRICE_DESC("Harga Tertinggi ▼"),
        RATING_DESC("Rating Tertinggi ▼")
    }

    private lateinit var binding: ActivityListDataBinding
    private lateinit var adapter: PaketTurAdapter
    private var currentCategory = CategoryFilter.ALL
    private var currentSort = SortMode.PRICE_ASC

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupCategoryChips()
        setupSortButton()

        binding.btnBack.setOnClickListener {
            finish()
        }

        val canCrud = ProfilActivity.canManageCrud(this)
        binding.fabAdd.visibility = if (canCrud) View.VISIBLE else View.GONE
        binding.fabAdd.setOnClickListener {
            if (ProfilActivity.canManageCrud(this)) {
                startActivity(Intent(this, FormInputActivity::class.java))
            } else {
                Toast.makeText(this, "Wisatawan tidak dapat menambah paket tur", Toast.LENGTH_SHORT).show()
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilterAndSort()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val initialQuery = intent.getStringExtra(EXTRA_QUERY)
        if (!initialQuery.isNullOrEmpty()) {
            binding.etSearch.setText(initialQuery)
            binding.etSearch.setSelection(initialQuery.length)
        }
        if (intent.getBooleanExtra(EXTRA_FOCUS_SEARCH, false)) {
            binding.etSearch.requestFocus()
        }

        applyFilterAndSort()
    }

    override fun onResume() {
        super.onResume()
        applyFilterAndSort()
    }

    private fun setupRecyclerView() {
        val canCrud = ProfilActivity.canManageCrud(this)
        adapter = PaketTurAdapter(
            listPaket = mutableListOf(),
            canManageCrud = canCrud,
            onItemClick = { item ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("EXTRA_PAKET", item)
                startActivity(intent)
            },
            onEditClick = { item ->
                if (ProfilActivity.canManageCrud(this)) {
                    val intent = Intent(this, FormUpdateActivity::class.java)
                    intent.putExtra("EXTRA_PAKET", item)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Wisatawan tidak dapat mengubah paket tur", Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.rvPaketTur.layoutManager = LinearLayoutManager(this)
        binding.rvPaketTur.adapter = adapter
    }

    private fun setupCategoryChips() {
        binding.chipSemua.setOnClickListener {
            currentCategory = CategoryFilter.ALL
            updateChipStyles()
            applyFilterAndSort()
        }
        binding.chipKuliner.setOnClickListener {
            currentCategory = CategoryFilter.FOOD
            updateChipStyles()
            applyFilterAndSort()
        }
        binding.chipBudaya.setOnClickListener {
            currentCategory = CategoryFilter.CULTURE
            updateChipStyles()
            applyFilterAndSort()
        }
    }

    private fun updateChipStyles() {
        styleChip(binding.chipSemua, currentCategory == CategoryFilter.ALL, "✓ Semua Kategori", "Semua Kategori")
        styleChip(binding.chipKuliner, currentCategory == CategoryFilter.FOOD, "✓ 🍴 Kuliner (Food)", "🍴 Kuliner (Food)")
        styleChip(binding.chipBudaya, currentCategory == CategoryFilter.CULTURE, "✓ 🏯 Budaya & Sejarah", "🏯 Budaya & Sejarah")
    }

    private fun styleChip(chip: TextView, isActive: Boolean, activeText: String, inactiveText: String) {
        val activeBg = ContextCompat.getDrawable(this, R.drawable.bg_pill_mint)
        val inactiveBg = ContextCompat.getDrawable(this, R.drawable.bg_pill_dark_green)
        val activeColor = ContextCompat.getColor(this, R.color.forest_green_dark)
        val inactiveColor = ContextCompat.getColor(this, R.color.white)

        chip.background = if (isActive) activeBg else inactiveBg
        chip.setTextColor(if (isActive) activeColor else inactiveColor)
        chip.text = if (isActive) activeText else inactiveText
    }

    private fun setupSortButton() {
        binding.tvSort.text = currentSort.label
        binding.tvSort.setOnClickListener {
            val options = arrayOf(
                "Harga Terendah",
                "Harga Tertinggi",
                "Rating Tertinggi",
                "Urutan Default"
            )
            AlertDialog.Builder(this)
                .setTitle("Urutkan Paket Tur")
                .setItems(options) { _, which ->
                    currentSort = when (which) {
                        0 -> SortMode.PRICE_ASC
                        1 -> SortMode.PRICE_DESC
                        2 -> SortMode.RATING_DESC
                        else -> SortMode.DEFAULT
                    }
                    binding.tvSort.text = currentSort.label
                    applyFilterAndSort()
                }
                .show()
        }
    }

    private fun applyFilterAndSort() {
        val query = binding.etSearch.text?.toString()?.trim().orEmpty()

        val filtered = WisataraRepository.getAll().filter { item ->
            val matchesQuery = query.isEmpty() ||
                item.judulTur.contains(query, ignoreCase = true) ||
                item.kotaDestinasi.contains(query, ignoreCase = true) ||
                item.namaPemandu.contains(query, ignoreCase = true) ||
                item.kategoriTur.contains(query, ignoreCase = true)

            val matchesCategory = when (currentCategory) {
                CategoryFilter.ALL -> true
                CategoryFilter.FOOD -> item.kategoriTur.contains("Food", ignoreCase = true) ||
                    item.kategoriTur.contains("Kuliner", ignoreCase = true)
                CategoryFilter.CULTURE -> item.kategoriTur.contains("History", ignoreCase = true) ||
                    item.kategoriTur.contains("Culture", ignoreCase = true) ||
                    item.kategoriTur.contains("Budaya", ignoreCase = true) ||
                    item.kategoriTur.contains("Heritage", ignoreCase = true)
            }

            matchesQuery && matchesCategory
        }

        val sorted = when (currentSort) {
            SortMode.PRICE_ASC -> filtered.sortedBy { it.hargaPerOrang }
            SortMode.PRICE_DESC -> filtered.sortedByDescending { it.hargaPerOrang }
            SortMode.RATING_DESC -> filtered.sortedByDescending { it.rating }
            SortMode.DEFAULT -> filtered
        }

        adapter.updateData(sorted)
        updateCountText(sorted.size)
    }

    private fun updateCountText(count: Int) {
        binding.tvCount.text = "• Menampilkan $count Paket Tur Privat Tersedia"
    }
}

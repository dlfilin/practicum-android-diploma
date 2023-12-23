package ru.practicum.android.diploma.filter.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemAreaCountryChooserBinding
import ru.practicum.android.diploma.filter.domain.models.Country

class CountryAdapter(private val itemClickListenerCountry: ItemClickListenerCountry) :
    RecyclerView.Adapter<CountryAdapter.CountryHolder>() {

    private val listItem = ArrayList<Country>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_area_country_chooser, parent, false)
        return CountryHolder(view)
    }

    override fun onBindViewHolder(holder: CountryHolder, position: Int) {
        holder.bind(listItem[position])
        holder.itemView.setOnClickListener {
            itemClickListenerCountry.onItemListener(listItem[position])
        }
    }

    override fun getItemCount(): Int = listItem.size

    class CountryHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ItemAreaCountryChooserBinding.bind(item)
        fun bind(country: Country) = with(binding) {
            name.text = country.name
        }
    }

    fun interface ItemClickListenerCountry {
        fun onItemListener(item: Country)
    }

    private class DiffCallback(
        private val oldList: List<Country>,
        private val newList: List<Country>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    fun updateData(newData: List<Country>) {
        val diffCallback = DiffCallback(listItem, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listItem.clear()
        listItem.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }
}

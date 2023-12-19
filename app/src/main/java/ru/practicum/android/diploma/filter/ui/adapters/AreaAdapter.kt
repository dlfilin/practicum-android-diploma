package ru.practicum.android.diploma.filter.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemAreaCountryChooserBinding
import ru.practicum.android.diploma.filter.domain.models.Area

class AreaAdapter(private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<AreaAdapter.AreaHolder>() {

    private var listItem: MutableList<Area> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_area_country_chooser, parent, false)
        return AreaHolder(view)
    }

    override fun onBindViewHolder(holder: AreaHolder, position: Int) {
        holder.bind(listItem[position], itemClickListener)
    }

    override fun getItemCount(): Int = listItem.size

    class AreaHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemAreaCountryChooserBinding.bind(item)
        fun bind(area: Area, onClickListener: ItemClickListener) = with(binding) {
            name.text = area.name

            itemView.setOnClickListener {
                onClickListener.onItemListener(area)
            }
        }
    }

    private class MyDiffCallback(
        private val oldList: List<Area>,
        private val newList: List<Area>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name == newList[newItemPosition].name
        }
    }

    fun interface ItemClickListener {
        fun onItemListener(item: Area)
    }

    fun updateData(newData: List<Area>) {
        val diffCallback = MyDiffCallback(listItem, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listItem.clear()
        listItem.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }
}

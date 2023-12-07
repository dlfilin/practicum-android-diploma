package ru.practicum.android.diploma.filter.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemIndustryChooserBinding
import ru.practicum.android.diploma.filter.domain.models.Industry

class IndustryAdapter(
    private val onItemCheckedListener: ((item: Industry) -> Unit)? = null
) : RecyclerView.Adapter<IndustryAdapter.IndustryHolder>() {

    private var mSelectedItem = -1

    var listItem: MutableList<Industry> = ArrayList()
    private var originalListItem: MutableList<Industry> = ArrayList()
    private val filterListItem: MutableList<Industry> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_industry_chooser, parent, false)
        return IndustryHolder(view)
    }

    override fun onBindViewHolder(holder: IndustryHolder, position: Int) {
        holder.bind(listItem[position])

        holder.radioButton.isChecked = listItem[position].isChecked
        holder.radioButton.setOnClickListener {

            mSelectedItem = holder.adapterPosition
            val listNew = listItem.mapIndexed { index, industry ->
                if (index == mSelectedItem) {
                    industry.copy(isChecked = true)
                } else {
                    industry.copy(isChecked = false)
                }
            }
            updateDisplayList(listNew)
            onItemCheckedListener?.invoke(listNew[mSelectedItem])
        }
    }

    override fun getItemCount(): Int = listItem.size

    inner class IndustryHolder(item: View) : RecyclerView.ViewHolder(item) {

        private val binding = ItemIndustryChooserBinding.bind(item)
        val radioButton = binding.radioButton

        fun bind(industry: Industry) = with(binding) {
            industryName.text = industry.name
            radioButton.isChecked = industry.isChecked
        }
    }

    private class MyDiffCallback(
        private val oldList: List<Industry>, private val newList: List<Industry>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].isChecked == newList[newItemPosition].isChecked
        }
    }

    fun updateData(newData: List<Industry>) {
        val diffCallback = MyDiffCallback(listItem, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listItem.clear()
        listItem.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
        originalListItem.clear()
        originalListItem.addAll(newData)
    }

    private fun updateDisplayList(updateList: List<Industry>) {
        val diffCallback = MyDiffCallback(listItem, updateList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listItem.clear()
        listItem.addAll(updateList)
        diffResult.dispatchUpdatesTo(this)

    }

    fun filter(searchQuery: String?) {
        filterListItem.clear()
        if (searchQuery.isNullOrBlank()) {
            updateDisplayList(originalListItem)
        } else {
            for (item in originalListItem) {
                if (item.name.contains(searchQuery, true)) {
                    filterListItem.add(item)
                }
            }
            updateDisplayList(filterListItem)
        }
    }

}

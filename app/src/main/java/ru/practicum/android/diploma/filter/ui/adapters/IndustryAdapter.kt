package ru.practicum.android.diploma.filter.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemIndustryChooserBinding
import ru.practicum.android.diploma.filter.presentation.models.IndustryUi

class IndustryAdapter(
    private var itemClickListener: ItemClickListener
) : RecyclerView.Adapter<IndustryAdapter.IndustryHolder>() {

    private var listItem: List<IndustryUi> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_industry_chooser, parent, false)
        return IndustryHolder(view)
    }

    override fun onBindViewHolder(holder: IndustryHolder, position: Int) {
        holder.bind(listItem[position], itemClickListener)
    }

    override fun getItemCount(): Int = listItem.size

    inner class IndustryHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemIndustryChooserBinding.bind(item)

        fun bind(industry: IndustryUi, onClickListener: ItemClickListener) = with(binding) {
            industryName.text = industry.name
            radioButton.isChecked = industry.isChecked

            itemView.setOnClickListener {
                onClickListener.onItemListener(industry)
            }
            radioButton.setOnClickListener {
                onClickListener.onItemListener(industry)
            }
        }
    }

    private class MyDiffCallback(
        private val oldList: List<IndustryUi>,
        private val newList: List<IndustryUi>
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

    fun updateData(newData: List<IndustryUi>) {
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(listItem, newData))
        listItem = newData
        diffResult.dispatchUpdatesTo(this)
    }

    fun interface ItemClickListener {
        fun onItemListener(item: IndustryUi)
    }
}

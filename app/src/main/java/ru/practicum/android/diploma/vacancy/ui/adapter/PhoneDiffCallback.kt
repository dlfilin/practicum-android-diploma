package ru.practicum.android.diploma.vacancy.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.vacancy.domain.models.Phone

class PhoneDiffCallback(
    private val oldList: List<Phone?>,
    private val newList: List<Phone?>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

package ru.practicum.android.diploma.search.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.util.Formatter
import ru.practicum.android.diploma.databinding.VacancyViewItemBinding
import ru.practicum.android.diploma.search.domain.model.VacancyItem

class SearchAdapter(private val clickListener: VacancyClickListener) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {


    private val listItems = ArrayList<VacancyItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_view_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(listItems[position])
        holder.itemView.setOnClickListener { clickListener.onVacancyClick(listItems[position]) }
    }

    override fun getItemCount(): Int = listItems.size

    fun interface VacancyClickListener {
        fun onVacancyClick(vacancy: VacancyItem)
    }

    class SearchViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = VacancyViewItemBinding.bind(item)
        fun bind(vacancy: VacancyItem) {
            with(binding) {
                vacancyNameAndCity.text =
                    itemView.resources.getString(R.string.vacancy_item_title, vacancy.vacancyName, vacancy.area)
                companyName.text = vacancy.employerName
                salary.text = Formatter.formatSalary(
                    itemView.context, vacancy.salaryFrom, vacancy.salaryTo, vacancy.salaryCurrency
                )
                Glide.with(itemView).load(vacancy.employerLogoUrl).placeholder(R.drawable.vacancy_item_placeholder)
                    .transform(
                        CenterInside(),
                        RoundedCorners(
                            itemView.resources.getDimensionPixelSize(R.dimen.size_12)
                        ),
                    ).into(employerLogoIv)
            }
        }
    }

    fun updateData(newData: List<VacancyItem>) {
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(listItems, newData))
        listItems.clear()
        listItems.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }
}


private class MyDiffCallback(
    private val oldList: List<VacancyItem>, private val newList: List<VacancyItem>
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
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

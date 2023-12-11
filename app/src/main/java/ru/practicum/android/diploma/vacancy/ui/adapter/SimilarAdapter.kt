package ru.practicum.android.diploma.vacancy.ui.adapter

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

class SimilarAdapter(private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<SimilarAdapter.ViewHolder>() {

    val similarList = ArrayList<VacancyItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(similarList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onItemListener(similarList[position])
        }
    }

    override fun getItemCount(): Int = similarList.size

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = VacancyViewItemBinding.bind(item)
        fun bind(vacancy: VacancyItem) = with(binding) {
            vacancyNameAndCity.text =
                itemView.resources.getString(R.string.vacancy_item_title, vacancy.vacancyName, vacancy.area)
            companyName.text = vacancy.employerName
            salary.text = Formatter.formatSalary(
                itemView.context,
                vacancy.salaryFrom,
                vacancy.salaryTo,
                vacancy.salaryCurrency
            )
            Glide.with(itemView)
                .load(vacancy.employerLogoUrl)
                .placeholder(R.drawable.vacancy_item_placeholder)
                .transform(
                    CenterInside(),
                    RoundedCorners(
                        itemView.resources.getDimensionPixelSize(R.dimen.size_12)
                    ),
                ).into(employerLogoIv)
        }
    }

    private class DiffSimilarCallback(private val oldList: List<VacancyItem>, private val newList: List<VacancyItem>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    fun addSimilarList(newData: List<VacancyItem>) {
        val diffCallback = DiffSimilarCallback(similarList, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        similarList.clear()
        similarList.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    fun interface ItemClickListener {
        fun onItemListener(item: VacancyItem)
    }
}




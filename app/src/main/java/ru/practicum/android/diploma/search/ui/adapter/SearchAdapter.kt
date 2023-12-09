package ru.practicum.android.diploma.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.util.Formatter
import ru.practicum.android.diploma.databinding.VacancyViewItemBinding
import ru.practicum.android.diploma.search.domain.model.VacancyItem

class SearchAdapter(private val clickListener: VacancyClickListener) : RecyclerView.Adapter<SearchViewHolder>() {

    private var vacancyList = listOf<VacancyItem>()

    fun setVacancyList(newList: List<VacancyItem>) {
        vacancyList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = VacancyViewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(vacancyList[position])
        holder.itemView.setOnClickListener { clickListener.onVacancyClick(vacancyList[position]) }
    }

    override fun getItemCount(): Int = vacancyList.size
    interface VacancyClickListener {
        fun onVacancyClick(vacancy: VacancyItem)
    }
}

class SearchViewHolder(
    private val binding: VacancyViewItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(vacancy: VacancyItem) {
        with(binding) {
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
}

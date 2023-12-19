package ru.practicum.android.diploma.favorites.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.VacancyViewItemBinding
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

class VacancyAdapter(
    private var onClickListener: OnClickListener
) : RecyclerView.Adapter<VacancyViewHolder>() {

    private var vacancies = listOf<Vacancy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VacancyViewItemBinding.inflate(inflater, parent, false)
        return VacancyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancies[position], onClickListener)
    }

    fun updateRecycleView(
        newVacancyList: List<Vacancy>
    ) {
        val diffResult = DiffUtil.calculateDiff(VacancyDiffCallback(vacancies, newVacancyList))
        vacancies = newVacancyList
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnClickListener {
        fun onVacancyClick(vacancy: Vacancy)
    }
}

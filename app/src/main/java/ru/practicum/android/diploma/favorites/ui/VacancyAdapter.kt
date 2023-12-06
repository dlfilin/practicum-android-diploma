package ru.practicum.android.diploma.favorites.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.VacancyViewItemBinding
import ru.practicum.android.diploma.favorites.domain.models.Favorite

class VacancyAdapter(
    private var onClickListener: OnClickListener
) : RecyclerView.Adapter<VacancyViewHolder>() {

    private var vacancies = ArrayList<Favorite>()

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
        newVacancyList: List<Favorite>,
        isPagination: Boolean = false
    ) {
        if (isPagination) {
            // TODO: для полной версии с пейджингом
        } else {
            // TODO: рефакторинг с diffutil
            vacancies.clear()
            vacancies.addAll(newVacancyList)
            notifyDataSetChanged()
        }
    }

    interface OnClickListener {
        fun onVacancyClick(vacancy: Favorite)
    }
}

package ru.practicum.android.diploma.favorites.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyViewItemBinding
import ru.practicum.android.diploma.favorites.domain.models.Favorite

class VacancyViewHolder(
    private val binding: VacancyViewItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val radiusForLogo = binding.root.resources.getDimensionPixelSize(R.dimen.size_12)

    fun bind(vacancy: Favorite, onClickListener: VacancyAdapter.OnClickListener) {
        with(binding) {
            Glide.with(itemView)
                .load(vacancy.logoUrl)
                .placeholder(R.drawable.vacancy_item_placeholder)
                .centerCrop()
                .transform(RoundedCorners(radiusForLogo))
                .into(binding.employerLogoIv)

            vacancyNameAndCity.text = buildString {
                append(vacancy.nameVacancies)
                vacancy.city?.takeIf { it.isNotEmpty() }?.let {
                    append(", $it")
                }
            }

            companyName.text = vacancy.nameCompany
            salary.text = "TODO после рзработки утилиты"
        }

        itemView.setOnClickListener {
            onClickListener.onVacancyClick(vacancy)
        }
    }
}

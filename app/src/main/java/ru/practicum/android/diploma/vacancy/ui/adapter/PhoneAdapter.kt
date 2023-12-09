package ru.practicum.android.diploma.vacancy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.common.util.Formatter
import ru.practicum.android.diploma.databinding.ItemPhoneBinding
import ru.practicum.android.diploma.vacancy.domain.models.Phone

class PhonesAdapter(
    private val clickListener: PhoneClickListener
) : RecyclerView.Adapter<PhoneViewHolder>() {

    private var phones = listOf<Phone?>()

    fun updatePhones(newList: List<Phone?>) {
        val diffResult =
            DiffUtil.calculateDiff(PhoneDiffCallback(phones, newList))

        phones = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PhoneViewHolder(ItemPhoneBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: PhoneViewHolder, position: Int) {
        phones[position]?.let { holder.bind(it) }
        holder.itemView.setOnClickListener {
            clickListener.onPhoneClick(phones[position]!!)
        }
    }

    override fun getItemCount(): Int {
        return phones.size
    }

    interface PhoneClickListener {
        fun onPhoneClick(phone: Phone)
    }
}

class PhoneViewHolder(
    private val binding: ItemPhoneBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(phone: Phone) {
        val formattedNumber = Formatter.formatPhone(
            phone.country,
            phone.city,
            phone.number
        )

        binding.apply {
            phoneValue.text = formattedNumber
            phoneComment.text = phone.comment
        }
    }
}

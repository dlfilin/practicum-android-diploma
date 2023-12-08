package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.util.Formatter
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.vacancy.domain.models.Phone
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy
import ru.practicum.android.diploma.vacancy.presentation.VacancyScreenState
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel
import ru.practicum.android.diploma.vacancy.ui.adapter.PhonesAdapter

class VacancyFragment : Fragment(R.layout.fragment_vacancy) {

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private var phonesAdapter: PhonesAdapter? = null
    private lateinit var onPhoneClickDebounce: (Phone) -> Unit
    private var isFavorite = false

    private val menuHost: MenuHost get() = requireActivity()

    private val viewModel by viewModel<VacancyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentVacancyBinding.bind(view)

//        val vacancyId = requireArguments().getString(ARG_VACANCY, "87930777")
//        val vacancyId = "87930777" // no logo
        val vacancyId = "88997708"
        viewModel.getVacancy(vacancyId)
        viewModel.observeVacancyState().observe(viewLifecycleOwner) {
            render(it)
        }


        binding.gotoSimilarJobsFragmentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_vacancyFragment_to_similarVacanciesFragment)
        }

        setObservables()
        prepareToolbarMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun render(state: VacancyScreenState) {
        when (state) {
            is VacancyScreenState.Loading -> showLoading()
            is VacancyScreenState.Error -> showError()
            is VacancyScreenState.Content -> showContent(state.vacancy)
        }
    }

    private fun showLoading() {
        Log.d("DEBUG", "Loading...")
//        TODO("should be implemented in another task")
    }

    private fun showError() {
        Log.d("DEBUG", "ERROR")
//        TODO("should be implemented in another task")
    }

    private fun showContent(vacancy: Vacancy) {
        binding.apply {
            nameVacancy.text = vacancy.vacancyName
            salary.text = Formatter.formatSalary(
                requireContext(),
                vacancy.salaryFrom,
                vacancy.salaryTo,
                vacancy.salaryCurrency
            )

            val radiusForLogo = binding.root.resources.getDimensionPixelSize(R.dimen.size_12)

            Glide.with(requireContext())
                .load(vacancy.employerLogoUrl)
                .placeholder(R.drawable.vacancy_item_placeholder)
                .centerCrop()
                .transform(RoundedCorners(radiusForLogo))
                .into(binding.image)

            companyName.text = vacancy.employerName
            city.text = if (vacancy.address.isNullOrEmpty()) vacancy.address else vacancy.area
            experience.text = vacancy.experience

            val employmentAndSchedueString = StringBuilder()
                .append(vacancy.employment)

            if (vacancy.employment != null && vacancy.schedule != null) {
                employmentAndSchedueString
                    .append(", ")
            }
            employmentAndSchedueString
                .append(vacancy.schedule)
            schedule.text = employmentAndSchedueString.toString()

            description.text = HtmlCompat.fromHtml(
                vacancy.description,
                FROM_HTML_MODE_LEGACY
            )

            if (vacancy.keySkills.isNullOrEmpty()) {
                tvKeySkill.visibility = View.GONE
                keySkill.visibility = View.GONE
            } else {
                val keySkillsString = buildString {
                    append("<ul>")
                    vacancy.keySkills.forEach { keySkill ->
                        append("<li>${keySkill}</li>")
                    }
                    append("</ul>")
                }
                keySkill.text = HtmlCompat.fromHtml(
                    keySkillsString,
                    FROM_HTML_MODE_LEGACY
                )
            }
            viewContactsSection(vacancy)
        }
    }

    private fun viewContactsSection(vacancy: Vacancy) {
        binding.apply {
            if (
                vacancy.contacts?.name?.isNotEmpty() == true ||
                vacancy.contacts?.email?.isNotEmpty() == true ||
                vacancy.contacts?.phones?.toString()?.isNotEmpty() == true
            ) {
                tvContacts.visibility = View.VISIBLE
            }

            if (vacancy.contacts?.name?.isNotEmpty() == true) {
                contactName.text = vacancy.contacts.name
                tvContactName.visibility = View.VISIBLE
                contactName.visibility = View.VISIBLE
            }

            if (vacancy.contacts?.email?.isNotEmpty() == true) {
                email.text = vacancy.contacts.email
                tvEmail.visibility = View.VISIBLE
                email.visibility = View.VISIBLE
            }

            Log.d("DEBUG", vacancy.contacts.toString())

            if (vacancy.contacts?.phones?.isNotEmpty() == true) {
                tvPhone.visibility = View.VISIBLE
                phonesAdapter = vacancy.contacts.phones.let {
                    PhonesAdapter(it) { phone ->
                        onPhoneClickDebounce(phone)
                    }
                }
                onPhoneClickDebounce = { phone ->
                    viewModel.makeCall(phone)
                }
                tvPhone.visibility = View.VISIBLE
                phoneList.adapter = phonesAdapter
                phoneList.visibility = View.VISIBLE
            }
        }
    }

    private fun prepareToolbarMenu() {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                val item = menu.findItem(R.id.action_toggle_favorite)
                val icon = if (isFavorite) {
                    R.drawable.ic_favorite_active
                } else {
                    R.drawable.ic_favorite_inactive
                }
                item.setIcon(icon)
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.vacancy_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_share -> {
                        Toast.makeText(requireContext(), R.string.share_vacancy, Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.action_toggle_favorite -> {
                        //viewmodel.toggleFavorite()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun setObservables() {
        /*viewmodel.state.observe(viewLifecycleOwner) { state ->
            isFavorite = state.vacancy
            // нужно дернуть тулбар в Activity, чтобы он перерисовался
            requireActivity().invalidateOptionsMenu()

        }*/

    }

    companion object {
        fun createArgs(vacancyId: String): Bundle =
            bundleOf(ARG_VACANCY to vacancyId)

        private const val ARG_VACANCY = "vacancy"
    }
}

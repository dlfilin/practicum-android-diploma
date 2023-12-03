package ru.practicum.android.diploma.root

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.util.CustomAction
import ru.practicum.android.diploma.common.util.HasCustomActions
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private var _binding: ActivityRootBinding? = null
    private val binding get() = _binding!!

    private var currentFragment: Fragment? = null

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is NavHostFragment) return
            currentFragment = f
            updateUi()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityRootBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        val appBarConfiguration =
            AppBarConfiguration(
                setOf(R.id.searchFragment, R.id.favoritesFragment, R.id.aboutFragment),
                fallbackOnNavigateUpListener = ::onSupportNavigateUp
            )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)


        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }

    override fun onDestroy() {
        super.onDestroy()

        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        updateUi()
        return true
    }

    private fun updateUi() {
        val fragment = currentFragment

        if (fragment is HasCustomActions) {
            createCustomToolbarAction(fragment.getCustomActions())
        } else {
            binding.toolbar.menu.clear()
        }
    }

    private fun createCustomToolbarAction(actions: List<CustomAction>) {
        binding.toolbar.menu.clear()

        actions.forEach {action ->
            val iconDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, action.iconRes)!!)
            iconDrawable.setTint(Color.BLACK)

            val menuItem = binding.toolbar.menu.add(action.textRes)
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            menuItem.icon = iconDrawable
            menuItem.setOnMenuItemClickListener {
                action.onCustomAction.run()
                return@setOnMenuItemClickListener true
            }
        }
    }
}

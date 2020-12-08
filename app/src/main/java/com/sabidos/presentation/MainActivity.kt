package com.sabidos.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sabidos.R
import com.sabidos.infrastructure.events.BottomNavigationEvent
import com.sabidos.infrastructure.events.EventBus
import com.sabidos.infrastructure.extensions.goTo
import com.sabidos.presentation.category.CategoryActivity
import com.sabidos.presentation.components.BottomNavigationComponent.NavModel
import com.sabidos.presentation.components.BottomNavigationPagerAdapter
import com.sabidos.presentation.components.BottomNavigationPagerAdapter.Companion.HOME_TAB_POSITION
import com.sabidos.presentation.components.BottomNavigationPagerAdapter.Companion.PREMIUM_TAB_POSITION
import com.sabidos.presentation.components.BottomNavigationPagerAdapter.Companion.PROFILE_TAB_POSITION
import com.sabidos.presentation.components.BottomNavigationPagerAdapter.Companion.RANKING_TAB_POSITION
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    private lateinit var subscription: ReceiveChannel<BottomNavigationEvent>

    private val bottomNavItems = listOf(
        NavModel(
            R.id.homeMenuItem,
            R.drawable.ic_home,
            R.string.home_label,
            true
        ) { mainViewPager.setCurrentItem(HOME_TAB_POSITION, false) },
        NavModel(
            R.id.premiumMenuItem,
            R.drawable.ic_rocket,
            R.string.premium_label
        ) { mainViewPager.setCurrentItem(PREMIUM_TAB_POSITION, false) },
        NavModel(
            R.id.rankingMenuItem,
            R.drawable.ic_trophy,
            R.string.ranking_label
        ) { mainViewPager.setCurrentItem(RANKING_TAB_POSITION, false) },
        NavModel(
            R.id.profileMenuItem,
            R.drawable.ic_profile,
            R.string.profile_label
        ) { mainViewPager.setCurrentItem(PROFILE_TAB_POSITION, false) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch { subscribeEvents() }
        setContentView(R.layout.activity_main)
        setupFragmentsViewPager()
        sabidosBottomNavMenu.items = bottomNavItems

        playNavButton.onClickCallback = {
            goTo(CategoryActivity::class.java, finished = false)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.syncQuiz()
    }

    private fun setupFragmentsViewPager() {
        val adapter = BottomNavigationPagerAdapter(this)
        mainViewPager.isUserInputEnabled = false
        mainViewPager.adapter = adapter
        mainViewPager.setCurrentItem(HOME_TAB_POSITION, false)
    }

    @ExperimentalCoroutinesApi
    private suspend fun subscribeEvents() = withContext(Dispatchers.Main) {
        subscription = EventBus.asChannel()
        subscription.consumeEach {
            bottomNavItems[it.menuOption].item?.tap()
        }
    }

    override fun onStop() {
        super.onStop()
        subscription.cancel()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
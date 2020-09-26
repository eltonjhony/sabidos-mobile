package com.sabidos.presentation.components

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sabidos.presentation.home.HomeFragment
import com.sabidos.presentation.premium.PremiumFragment
import com.sabidos.presentation.profile.ProfileFragment
import com.sabidos.presentation.ranking.RankingFragment

class BottomNavigationPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    companion object {
        const val SIZE = 4
        const val HOME_TAB_POSITION = 0
        const val PREMIUM_TAB_POSITION = 1
        const val RANKING_TAB_POSITION = 2
        const val PROFILE_TAB_POSITION = 3
    }

    override fun getItemCount(): Int = SIZE

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            HOME_TAB_POSITION -> HomeFragment()
            PREMIUM_TAB_POSITION -> PremiumFragment()
            RANKING_TAB_POSITION -> RankingFragment()
            else -> ProfileFragment()
        }
    }

}
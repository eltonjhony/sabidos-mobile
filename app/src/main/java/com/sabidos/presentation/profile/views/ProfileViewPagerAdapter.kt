package com.sabidos.presentation.profile.views

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sabidos.R
import com.sabidos.presentation.profile.AccountManagementFragment
import com.sabidos.presentation.profile.MyPerformanceFragment

class ProfileViewPagerAdapter(
    private val context: Context?,
    fa: FragmentManager,
    behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) : FragmentPagerAdapter(fa, behavior) {

    companion object {
        const val SIZE = 2
        const val ACCOUNT_MANAGEMENT_TAB_POSITION = 0
        const val PERFORMANCE_TAB_POSITION = 1
    }

    override fun getCount(): Int = SIZE

    override fun getItem(position: Int): Fragment {
        return when (position) {
            ACCOUNT_MANAGEMENT_TAB_POSITION -> AccountManagementFragment()
            else -> MyPerformanceFragment()
        }
    }

    override fun getPageTitle(position: Int): String =
        when (position) {
            ACCOUNT_MANAGEMENT_TAB_POSITION -> context?.getString(R.string.manage_account_label)
                ?: ""
            PERFORMANCE_TAB_POSITION -> context?.getString(R.string.my_performance_label) ?: ""
            else -> ""
        }

}
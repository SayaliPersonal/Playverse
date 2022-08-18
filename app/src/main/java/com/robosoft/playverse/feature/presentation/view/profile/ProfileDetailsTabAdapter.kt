package com.robosoft.playverse.feature.presentation.view.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfileDetailsTabAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ProfileTournamentsFragment()
            }
            1 -> {
                ProfileReferralsFragment()
            }
            else -> ProfileTournamentsFragment()
        }
    }

}
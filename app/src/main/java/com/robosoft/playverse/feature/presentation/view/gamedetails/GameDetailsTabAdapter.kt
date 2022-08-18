package com.robosoft.playverse.feature.presentation.view.gamedetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class GameDetailsTabAdapter (fa: FragmentActivity, private val id : String) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                GameModesFragment(id)
            }
            1 -> {
                HallOfFameFragment()
            }
            else -> HallOfFameFragment()
        }
    }

}
package com.robosoft.playverse.feature.presentation.view.funds

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.robosoft.playverse.feature.presentation.view.profile.ProfileTournamentsFragment

class TransactionsHistoryTabAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                MyTransactionsFragment()
            }
            1 -> {
                MyTransactionsFragment()
            }
            2 -> {
                MyTransactionsFragment()
            }
            3 -> {
                MyTransactionsFragment()
            }
            else -> MyTransactionsFragment()
        }
    }

}
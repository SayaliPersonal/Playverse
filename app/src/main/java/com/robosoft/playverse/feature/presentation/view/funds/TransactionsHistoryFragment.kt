package com.robosoft.playverse.feature.presentation.view.funds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentTransactionsHistoryBinding
import com.robosoft.playverse.feature.presentation.view.profile.ProfileDetailsTabAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionsHistoryFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsHistoryBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentTransactionsHistoryBinding.inflate(inflater,container,false)
        subscribeUI()
        bindUI()
        return binding.root
    }

    private fun bindUI() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun subscribeUI() {
        val adapter = TransactionsHistoryTabAdapter(requireActivity())
        binding.vpTransaction.adapter = adapter
        binding.vpTransaction.isUserInputEnabled = false
        binding.vpTransaction.let {
            TabLayoutMediator(binding.tabTransaction, it) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.all)
                    1 -> tab.text = getString(R.string.deposited)
                    2 ->tab.text = getString(R.string.withdrawals)
                    3 ->tab.text = getString(R.string.rewards)
                }
            }.attach()
        }

        for (i in 0 until binding.tabTransaction.tabCount) {
            val tab = (binding.tabTransaction.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(10, 10, 10, 10)
            tab.requestLayout()
        }
    }

}
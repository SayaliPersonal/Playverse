package com.robosoft.playverse.feature.presentation.view.funds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentMyFundsBinding
import com.robosoft.playverse.feature.presentation.viewModel.MyFundsViewModel
import com.robosoft.playverse.utilities.Connectivity
import com.robosoft.playverse.utilities.TemporaryStorage
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class MyFundsFragment : Fragment() {

    private lateinit var binding: FragmentMyFundsBinding

    private val viewModel : MyFundsViewModel by viewModels()
    private lateinit var winningcash : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyFundsBinding.inflate(inflater, container, false)
        bindUI()
        subscribeUI()
        return binding.root
    }

    private fun subscribeUI() {
        viewModel.cashBalance.observe(viewLifecycleOwner){
               val number = DecimalFormat("###,###,##0.00")
               binding.tvTotalCash.text = getString(R.string.cash,(number.format(it.data?.totalCash)?.toString()))
               binding.tvDepositedCashAmount.text = getString(R.string.cash,(number.format(it.data?.depositCash)?.toString()))
               binding.tvWinningCashAmount.text = getString(R.string.cash,(number.format(it.data?.winningCash)?.toString()))
             winningcash =it.data?.winningCash.toString()
        }
    }


    private fun bindUI() {

        TemporaryStorage.networkError.value = Connectivity.isNetworkAvailable(requireContext())
        if(TemporaryStorage.networkError.value == false){
            binding.networkErrorValue = true
        }

        binding.networkError.btTryAgain.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }


        binding.btnAddCash.setOnClickListener {
            findNavController().navigate(R.id.action_myFundsFragment_to_addCashFragment)
        }

        binding.btnWithdraw.setOnClickListener {
            val action =
                MyFundsFragmentDirections.actionMyFundsFragmentToWithdrawCashFragment(
                    winningCash = winningcash
                )
            findNavController().navigate(action)
        }

        binding.ivRightArrow.setOnClickListener {
            findNavController().navigate(R.id.transactionsHistoryFragment)
        }
    }

}
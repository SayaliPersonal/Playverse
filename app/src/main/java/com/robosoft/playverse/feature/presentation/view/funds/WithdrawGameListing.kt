package com.robosoft.playverse.feature.presentation.view.funds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentWithdrawGameListingBinding
import com.robosoft.playverse.feature.presentation.view.gamedetails.GameListAdapter
import com.robosoft.playverse.feature.presentation.viewModel.DepositGameListingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.deposit_screen.view.*
import kotlinx.android.synthetic.main.deposit_screen.view.txt_tds_val
import kotlinx.android.synthetic.main.deposit_screen.view.txt_transactionId
import kotlinx.android.synthetic.main.deposit_screen.view.txt_wallet_bal
import kotlinx.android.synthetic.main.withdraw_help_section.view.*
import kotlinx.android.synthetic.main.withdraw_screen.view.*


@AndroidEntryPoint
class WithdrawGameListing : Fragment(), GameListItemClickListener {

    lateinit var binding: FragmentWithdrawGameListingBinding

    private val mainViewModel: DepositGameListingViewModel by viewModels()
    private val args: WithdrawGameListingArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWithdrawGameListingBinding.inflate(inflater, container, false)

        updateWithdrawUI()

        mainViewModel.getDepositGameListing("2","5")

        mainViewModel.res.observe(viewLifecycleOwner) {
            binding.rvGameListData.layoutManager =
                LinearLayoutManager(requireContext())
            binding.rvGameListData.adapter = GameListAdapter(it.data.games, this)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_withdrawGameListing_to_homeFragment)

        }

        binding.tournamentConstraint.btn_back_home.setOnClickListener {
            findNavController().navigate(R.id.action_withdrawGameListing_to_homeFragment)
        }

        return binding.root
    }

    override fun getGameId(position: Int, gameId: Int) {
        val action = WithdrawGameListingDirections.actionWithdrawGameListingToGameDetailsFragment(
            gameId.toString()
        )
        findNavController().navigate(action)
    }

    private fun updateWithdrawUI() {
        binding.withdraw.txt_withdraw?.text = args.cashWithdrawn
        binding.withdraw.txt_withdrawn_val?.text = args.cashWithdrawn
        binding.withdraw.txt_wallet_bal?.text =args.processingFee
        binding.withdraw.txt_tds_val?.text = args.tds
        binding.withdraw.txt_amt_rec?.text = args.amountReceived
        binding.withdraw.txt_transactionId?.text =args.withdrawalId
    }

}
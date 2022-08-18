package com.robosoft.playverse.feature.presentation.view.funds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentDepositGameListingBinding
import com.robosoft.playverse.feature.presentation.view.gamedetails.GameListAdapter
import com.robosoft.playverse.feature.presentation.viewModel.DepositGameListingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.deposit_screen.*
import kotlinx.android.synthetic.main.deposit_screen.view.*
import kotlinx.android.synthetic.main.withdraw_help_section.view.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DepositGameListing : Fragment(), GameListItemClickListener {

    lateinit var binding: FragmentDepositGameListingBinding

    private val mainViewModel: DepositGameListingViewModel by viewModels()
    private val args: DepositGameListingArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDepositGameListingBinding.inflate(inflater, container, false)

        updateAddCashUI()

        mainViewModel.getDepositGameListing("2","5")

        mainViewModel.res.observe(viewLifecycleOwner) {
            binding.rvGameListData.layoutManager =
                LinearLayoutManager(requireContext())
            binding.rvGameListData.adapter = GameListAdapter(it.data.games, this)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_depositGameListing_to_homeFragment)

        }

        binding.tournamentConstraint.btn_back_home.setOnClickListener {
            findNavController().navigate(R.id.action_depositGameListing_to_homeFragment)
        }

        return binding.root
    }

    override fun getGameId(position: Int, gameId: Int) {
        val action = DepositGameListingDirections.actionDepositGameListingToGameDetailsFragment(
            gameId.toString()
        )
        findNavController().navigate(action)
    }

    private fun updateAddCashUI() {
        binding.deposit.txt_deposit.text = args.cashAdded
        binding.deposit.txt_amt_val?.text = args.cashAdded
        binding.deposit.txt_wallet_bal?.text =args.walletBalance
        binding.deposit.txt_tds_val?.text = args.paymentTime
        binding.deposit.txt_transactionId?.text = args.transactionId
    }

}
package com.robosoft.playverse.feature.presentation.view.funds

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.playverse.data.models.CashData
import com.playverse.data.models.TournamentsItem
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentAddCashBinding
import com.robosoft.playverse.feature.presentation.view.gamedetails.GameDetailsFragmentDirections
import com.robosoft.playverse.feature.presentation.viewModel.AddCashViewModel
import com.robosoft.playverse.utilities.Connectivity
import com.robosoft.playverse.utilities.TemporaryStorage

import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddCashFragment : Fragment() {
    private lateinit var binding: FragmentAddCashBinding
    private val addCashViewModel : AddCashViewModel by viewModels()

    @Inject
    lateinit var appStorage: AppStorage

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(addcash: Editable?) {
            if(addcash?.isNotEmpty() == true) {
                val number = DecimalFormat("###,###,##0.00")
                var cc = addcash.toString()
                if(cc == ".") cc = "0.0"
                val credits = number.format(cc.toDouble())
                if(addcash?.contains(" ") == true){
                    binding.tvErrorMsg.isVisible = true
                }
                binding.tvAlternative.isVisible = true
                binding.tvAlternative.text = credits
                binding.btnProgress.setOnClickListener {
                   if(addcash.toString()==""){
                       binding.tvErrorMsg.isVisible = true
                   }else{
                       addCashAmount(addcash.toString())
                   }
                }

            }else{
                binding.tvAlternative.isGone = true
            }


        }

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCashBinding.inflate(inflater, container, false)
        bindUI()


        return binding.root
    }

    private fun addCashAmount(addcash :String) {
        addCashViewModel.addCashBalance(addcash.toInt(),appStorage.userId.toInt())

        addCashViewModel.addCashFailure.observe(viewLifecycleOwner) {
            if (it.errorCode == 404 || it.errorCode == 500 || it.errorCode == 400) {
                binding.tvErrorMsg.isVisible = true
                binding.tvErrorMsg.text = getString(R.string.error_cash)
            }
        }

        addCashViewModel.addCash.observe(viewLifecycleOwner) {
            if(it?.success == true){
                TemporaryStorage.networkError.value = Connectivity.isNetworkAvailable(requireContext())
                if(TemporaryStorage.networkError.value == false){
                    binding.networkErrorValue = true
                }else {
                    val action =
                        AddCashFragmentDirections.actionAddCashFragmentToDepositGameListing(
                            cashAdded = it.data.cashAdded.toString(),
                            paymentTime = it.data.paymentTime,
                            transactionId = it.data.transactionId,
                            walletBalance = it.data.walletBalance.toString()
                        )
                    findNavController().navigate(action)
                   // findNavController().navigate(R.id.action_addCashFragment_to_depositGameListing)
                }
            }
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
        binding.ivBack.setOnClickListener { findNavController().navigate(R.id.myFundsFragment) }

        binding.etEnterAmount.addTextChangedListener(mTextEditorWatcher)
    }



}
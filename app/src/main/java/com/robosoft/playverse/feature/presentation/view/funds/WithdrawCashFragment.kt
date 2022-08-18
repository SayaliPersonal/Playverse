package com.robosoft.playverse.feature.presentation.view.funds

import android.graphics.Color
import android.graphics.Color.RED
import android.hardware.camera2.params.RggbChannelVector.RED
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentWithdrawCashBinding
import com.robosoft.playverse.feature.presentation.viewModel.WithdrawCahViewModel
import com.robosoft.playverse.utilities.debugLog
import com.robosoft.playverse.utilities.toast

import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class WithdrawCashFragment : Fragment() {
    private lateinit var binding: FragmentWithdrawCashBinding
    private val withdrawCahViewModel : WithdrawCahViewModel by viewModels()
    private val args: WithdrawCashFragmentArgs by navArgs()
    @Inject
    lateinit var appStorage: AppStorage
    val number = DecimalFormat("###,###,##0.00")


    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(withdrawCash: Editable?) {
            if(withdrawCash?.isNotEmpty() == true) {
                val number = DecimalFormat("###,###,##0.00")
                var cc = withdrawCash.toString()
                if(cc == ".") cc = "0.0"
                val credits = number.format(cc.toDouble())
                if(withdrawCash?.contains(" ") == true){

                }
                binding.tvAlternative.isVisible = true
                binding.tvAlternative.text = credits

                binding.continuebtn?.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                  // do nothing
                }

                override fun onStartTrackingTouch(seek: SeekBar) {
                    // do nothing
                }

                override fun onStopTrackingTouch(seek: SeekBar) {
                    if(withdrawCash.toString()==""){
                     //   binding.errortxt.isVisible = true
                   //     binding.errortxt.text= getString(R.string.min_max,(number.format(args.winningCash)?.toString()))
                    }else{
                        withdrawAmount(withdrawCash.toString())
                    }

                }
            })
            }else {
                binding.tvAlternative.isGone = true

            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWithdrawCashBinding.inflate(inflater,container,false)
        bindUI()
        return binding.root
    }

    private fun bindUI() {
        try {
            val cash =args.winningCash.toInt()
            binding.textView5.text = getString(R.string.cash,(number.format(cash)?.toString()))
            binding.errortxt.text= getString(R.string.min_max,(number.format(cash)?.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        binding.etEnterAmount.addTextChangedListener(mTextEditorWatcher)

        binding.tvLinkAccount.setOnClickListener {
            findNavController().navigate(R.id.upiLinkFragment)
        }
    }

    private fun withdrawAmount(withdrawCash :String) {

        withdrawCahViewModel.withdrawCash(withdrawCash.toInt(),appStorage.userId.toInt())
          withdrawCahViewModel.withdrawFailure.observe(viewLifecycleOwner) {
              if (it.errorCode == 404 || it.errorCode == 500 || it.errorCode == 400) {
                //  binding.errortxt.isVisible = true
                  binding.errortxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.error));
                  if(args.winningCash.toInt() < withdrawCash.toInt()){
                      binding.errortxt.text = getString(R.string.error_withdrawal_exceed,number.format(args.winningCash.toInt())?.toString())
                  }else{
                      binding.errortxt.text = getString(R.string.error_withdrawal)
                  }

              }
          }


        withdrawCahViewModel.withdrawCash.observe(viewLifecycleOwner) {
            if(it?.success == true){
                val action =
                    WithdrawCashFragmentDirections.actionWithdrawCashFragmentToWithdrawGameListing(
                        cashWithdrawn = it.data.cashWithdrawn.toString(),
                        processingFee = it.data.processingFee.toString(),
                        tds = it.data.tds.toString(),
                        amountReceived = it.data.amountReceived.toString(),
                        withdrawalId = it.data.withdrawalId
                    )
                findNavController().navigate(action)
                }else{
                    toast("failure")
                }
            }

        }
    }



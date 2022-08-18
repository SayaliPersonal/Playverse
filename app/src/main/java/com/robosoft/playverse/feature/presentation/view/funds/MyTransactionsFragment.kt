package com.robosoft.playverse.feature.presentation.view.funds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentMyTransactionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTransactionsFragment : Fragment() {
    private lateinit var binding : FragmentMyTransactionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyTransactionsBinding.inflate(inflater,container,false)
        setUpAdapter()

        return binding.root
    }

    private fun setUpAdapter() {

        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        val data = ArrayList<MyTransModel>()
        data.add(MyTransModel(1,money= "+₹5"))
        data.add(MyTransModel(2,money= "-₹5"))
        data.add(MyTransModel(2,money= "-₹5"))
        data.add(MyTransModel(2,money= "-₹5"))
        data.add(MyTransModel(1,money= "+₹5"))
        data.add(MyTransModel(1,money= "+₹5"))
        val adapter = MyTransactionAdapter(data)
        binding.rvTransactions.adapter = adapter
    }


}
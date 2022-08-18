package com.robosoft.playverse.feature.presentation.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentHomeBinding
import com.robosoft.playverse.feature.presentation.adapter.ViewPagerAdapter
import com.robosoft.playverse.feature.presentation.viewModel.HomeViewModel
import com.robosoft.playverse.utilities.Connectivity
import com.robosoft.playverse.utilities.TemporaryStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.header_footer.*
import kotlinx.android.synthetic.main.header_footer.view.*
import java.text.DecimalFormat
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel : HomeViewModel by viewModels()
    private val args: HomeFragmentArgs by navArgs()

    val tabArray = arrayOf(
        "Free to play",
        "Tournaments",
    )
    @Inject
    lateinit var appStorage: AppStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupTabLayout()
        bindUI()
    }


    private fun bindUI() {
        val number = DecimalFormat("###,###,##0")
        if (appStorage.userId.isEmpty()) {
            binding.header.coin.text = getString(R.string.dashhome)
        }else {
            viewModel.cashBalance.observe(viewLifecycleOwner) {
                binding.header.coin.text =
                    getString(R.string.cash,(number.format(it.data?.totalCash)?.toString()))
            }
        }
        binding.header.coin.setOnClickListener {
            if (appStorage.userId.isEmpty()) {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_myFundsFragment)
            }

        }
        binding.header.prize.setOnClickListener {
            if (appStorage.userId.isEmpty()) {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_myFundsFragment)
            }
        }

        TemporaryStorage.networkError.value = Connectivity.isNetworkAvailable(requireContext())
        if(TemporaryStorage.networkError.value == false){
            binding.networkErrorValue = true
        }

        binding.networkError.btTryAgain.setOnClickListener {
            binding.networkErrorValue = false
            setupViewPager()
            setupTabLayout()
            bindUI()

        }
    }
    private fun setupTabLayout() {
        TabLayoutMediator(
            sliding_tabs, vpager
        ) { tab, position -> tab.text = tabArray[position]
            if(args.fromProfileTournaments) {
                vpager.postDelayed(Runnable { vpager.currentItem = 1 }, 100)
            }
        }.attach()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(this, 2)
        vpager.adapter = adapter

    }


}

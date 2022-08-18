package com.robosoft.playverse.feature.presentation.view.getStarted

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentGetStartedBinding
import com.robosoft.playverse.utilities.Connectivity
import com.robosoft.playverse.utilities.TemporaryStorage


class GetStartedFragment : Fragment() {

    lateinit var imageList: List<Int>
    private lateinit var binding: FragmentGetStartedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGetStartedBinding.inflate(inflater, container, false)
        imageList = ArrayList()
        imageList = imageList + R.drawable.get_started1
        imageList = imageList + R.drawable.get_started

        TemporaryStorage.networkError.value = Connectivity.isNetworkAvailable(requireContext())
        if(TemporaryStorage.networkError.value == false){
            binding.networkErrorValue = true
        }

        binding.networkError.btTryAgain.setOnClickListener {
            findNavController().navigate(R.id.getStartedFragment)
        }



        val adapter = GetStartedAdapter (imageList,this)
        binding.viewPager .adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager)
        binding.viewPager.autoScrollOnBoarding(3000)
        binding.btNext.setOnClickListener {
            findNavController().navigate(R.id.action_getStartedFragment_to_homeFragment)
        }
        return binding.root

    }

    fun ViewPager.autoScrollOnBoarding(interval: Long) {

        val handler = Handler(Looper.getMainLooper())
        var scrollPosition = 0

        val runnable = object : Runnable {

            override fun run() {
                val count = adapter?.count ?: 0
                setCurrentItem(scrollPosition++ % count, true)
                handler.postDelayed(this, interval)
            }
        }
        addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                // Updating "scroll position" when user scrolls manually
                scrollPosition = position + 1
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Not necessary

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // Not necessary
            }
        })

        handler.post(runnable)
    }
}
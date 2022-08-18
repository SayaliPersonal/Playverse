package com.robosoft.playverse.feature.presentation.view.search

import android.graphics.BlurMaskFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.playverse.data.models.SearchMainResponse
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding : FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentSearchBinding.inflate(inflater,container,false)
        bindUI()
        return binding.root
    }

    private fun bindUI() {
        /*val filter = BlurMaskFilter(10F , BlurMaskFilter.Blur.INNER)
        binding.tvSearch.paint.maskFilter = filter*/
        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = SearchMainAdapter(createList())
        }
        binding.rvDiscover.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = SearchDiscoverAdapter(createDiscoverList())
        }
        binding.rvFeatured.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = SearchMainAdapter(createList())
        }
        binding.rvTry.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = SearchMainAdapter(createList())
        }


        binding.etSearch.setOnClickListener {
            findNavController().navigate(R.id.searchResultFragment)
        }

        binding.btnViewAll.setOnClickListener {
            findNavController().navigate(R.id.searchViewAllFragment)
        }
    }

    private fun createList(): ArrayList<SearchMainResponse> {
        return arrayListOf<SearchMainResponse>(
            SearchMainResponse(
                "Crossy Road",
                R.drawable.cover
            ),
            SearchMainResponse(
                "Game1",
                R.drawable.cover
            ),
            SearchMainResponse(
                "Game2",
                R.drawable.cover
            ),
        )
    }

    private fun createDiscoverList(): ArrayList<SearchMainResponse> {
        return arrayListOf<SearchMainResponse>(
            SearchMainResponse(
                "Action",
                null
            ),
            SearchMainResponse(
                "Games",
                null
            ),
            SearchMainResponse(
                "Strategy",
                null
            ),
            SearchMainResponse(
                "Strategy",
                null
            ),
            SearchMainResponse(
                "Strategy",
                null
            ),
        )
    }


    }
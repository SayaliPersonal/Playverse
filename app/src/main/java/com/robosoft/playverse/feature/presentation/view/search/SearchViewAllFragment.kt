package com.robosoft.playverse.feature.presentation.view.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.playverse.data.models.SearchMainResponse
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentSearchViewAllBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchViewAllFragment : Fragment() {

    private lateinit var binding: FragmentSearchViewAllBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentSearchViewAllBinding.inflate(inflater, container, false)
        bindUI()
        return binding.root
    }

    private fun bindUI() {
        binding.rvViewAll.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = SearchViewAllAdapter(createList())
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


}
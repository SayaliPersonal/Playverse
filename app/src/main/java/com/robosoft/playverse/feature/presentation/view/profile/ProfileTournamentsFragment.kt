package com.robosoft.playverse.feature.presentation.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.playverse.data.models.DataItem
import com.playverse.data.models.ProfileTournamentResponse
import com.playverse.data.util.Constants
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentProfileTournamentsBinding
import com.robosoft.playverse.feature.domain.model.ExoPlayerItem
import com.robosoft.playverse.feature.presentation.viewModel.ProfileViewModel
import com.robosoft.playverse.utilities.debugLog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/** ProfileTournamentsFragment class*/
@AndroidEntryPoint
class ProfileTournamentsFragment : Fragment(), ProfileClickListener {

    private lateinit var binding: FragmentProfileTournamentsBinding
    private var adapter: ProfileTournamentsAdapter? = null

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var appStorage: AppStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileTournamentsBinding.inflate(inflater, container, false)
        subscribeUI()
        setUpAdapter()
        return binding.root
    }

    private fun setUpAdapter() {
        adapter = ProfileTournamentsAdapter(requireContext(), this)
        binding.rvTournaments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTournaments.adapter = adapter
    }

    private fun subscribeUI() {
        viewModel.profileTournaments.observe(viewLifecycleOwner) {
            if (it.data?.isEmpty() == true) {
                binding.noTournaments.layout.isVisible = true
                binding.noTournaments.btn.setOnClickListener {
                    val action =
                        ProfileTournamentsFragmentDirections.actionProfileTournamentsFragmentToHomeFragment(
                            true
                        )
                    findNavController().navigate(action)
                }
            } else {
                val sortData = it.data?.sortedByDescending { Item->
                    Item.start_time
                }
                adapter?.setListData(sortData)
            }
        }
    }

    override fun onViewStandingsClick(id: String, data: DataItem) {
        val action =
            ProfileTournamentsFragmentDirections.actionProfileTournamentsFragmentToGameRecentRewardsAndLeaderFragment(
                gameId = id,
                tournamentsItem = data,
                fromProfileTournaments = true,
                viewStandingsClick = true
            )
        findNavController().navigate(action)
    }

    override fun onGameLoad(url: String) {

        //  findNavController().navigate(R.id.profileWebViewFragment)
    }

    override fun onStatsClick(id: String, data: DataItem, gameId: String) {
        val action =
            ProfileTournamentsFragmentDirections.actionProfileTournamentsFragmentToGameRecentRewardsAndLeaderFragment(
                gameId = id,
                tournamentsItem = data,
                fromProfileTournaments = true,
                tournamentId = gameId
            )
        findNavController().navigate(action)
    }

    override fun getItemData(position: Int, profileTournamentResponse: ProfileTournamentResponse) {
        var gameMode = ""
        gameMode = when (profileTournamentResponse.category) {
            0 -> {
                Constants.FREE_PLAY_MODE
            }
            else -> {
                Constants.TOURNAMENT_MODE
            }
        }
        viewModel.fetchSessionIdForGame(
            context = requireActivity(),
            gameId = profileTournamentResponse.game_id.toString(),
            userId = appStorage.userId,
            gameModeType = gameMode,
            gameModeId = profileTournamentResponse.id.toString()
        )
        viewModel.sessionId.observe(viewLifecycleOwner) {
            if (it.success) {
                val action =
                    ProfileTournamentsFragmentDirections.actionProfileTournamentsFragmentToGameFragment(
                        profileTournamentResponse.game_id.toString(),
                        gameurl = profileTournamentResponse.game_url,
                        sessionId = it.data.sessionId
                    )
                findNavController().navigate(action)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }


}




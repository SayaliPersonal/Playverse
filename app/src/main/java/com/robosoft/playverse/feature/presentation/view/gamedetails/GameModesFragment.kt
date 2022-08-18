package com.robosoft.playverse.feature.presentation.view.gamedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.playverse.data.models.DataItem
import com.playverse.data.models.TournamentsItem
import com.playverse.data.util.Constants
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.core.util.AlertDialogUtil
import com.robosoft.playverse.databinding.FragmentGameModesBinding
import com.robosoft.playverse.feature.presentation.view.home.BuyInClickListener
import com.robosoft.playverse.feature.presentation.viewModel.GameDetailsViewModel
import com.robosoft.playverse.utilities.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GameModesFragment(val tournamentId: String) : Fragment(), TournamentItemRemoveListener,
    TournamentStatsClickListener,
    GameClickListener, BuyInClickListener, TournamentGameClickListener, FreeGameClickListener,
    RecentGamesStatsClickListener,
    RecentItemRemoveListener {
    private lateinit var binding: FragmentGameModesBinding

    @Inject
    lateinit var appStorage: AppStorage

    private val mainViewModel: GameDetailsViewModel by viewModels()

    var recentGameList: ArrayList<DataItem> = ArrayList()
    var tournamentsList: ArrayList<TournamentsItem> = ArrayList()
    private var gameUrl: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameModesBinding.inflate(inflater, container, false)
        bindUI()
        return binding.root
    }

    private fun bindUI() {
        mainViewModel.getBannerAndOtherInfo(tournamentId)
        if (appStorage.userId.isNotEmpty()) {
            binding.constraintRecent.visible(visible = true)
            mainViewModel.getRecentGames(tournamentId, appStorage.userId)
        } else {
            binding.constraintRecent.visible(visible = false)
        }

        mainViewModel.recentGames.observe(viewLifecycleOwner) {
            recentGameList = it.data
            if (recentGameList.size == 0) {
                binding.constraintRecent.visible(visible = false)
            } else {
                binding.constraintRecent.visible(visible = true)
            }
            binding.rvRecentGameList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvRecentGameList.adapter = RecentGamesAdapter(it.data, this, this)

        }

        mainViewModel.res.observe(viewLifecycleOwner) { game ->
            tournamentsList = game.data.tournaments
            gameUrl = game.data.gameUrl

            if (game.data.tournaments.size == 0) {
                binding.tournamentConstraint.visible(false)
            } else {
                binding.tournamentConstraint.visible(true)
                binding.rvPlayTournament.layoutManager =
                    LinearLayoutManager(requireContext())
                binding.rvPlayTournament.adapter =
                    TournamentGameListAdapter(
                        game.data.tournaments,
                        this,
                        this,
                        this,
                        this,
                        this,
                        gameUrl,
                        tournamentId
                    )
            }
            if (game.data.freeGamePlays.isNotEmpty()) {
                binding.txtTournament.text = getString(R.string.or_win_big_in_the_tournaments)
                binding.rvFreePlaytGameList.layoutManager =
                    LinearLayoutManager(requireContext())
                binding.rvFreePlaytGameList.adapter =
                    GameFreePlayAdapter(game.data.freeGamePlays, this)
                binding.constraintTransparent.visible(visible = true)
            } else {
                binding.txtTournament.text = getString(R.string.win_big_in_the_tournaments)
                binding.constraintTransparent.visible(visible = false)
            }
        }
    }

    override fun tournamentPosition(position: Int) {
        try {
            tournamentsList.removeAt(position)
            binding.rvPlayTournament.apply {
                adapter?.notifyDataSetChanged()
            }
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    override fun getStatsItemPosition(position: Int, tournamentsItem: TournamentsItem) {
        val action =
            GameDetailsFragmentDirections.actionGameDetailsFragmentToGameRewardsAndLeaderFragment(
                tournamentsItem = tournamentsItem,
                gameId = tournamentsItem.id.toString(),
                gameUrl = gameUrl,
                tournamentId = tournamentId
            )
        findNavController().navigate(action)

    }

    override fun gameLoad(id: String, gameUrl: String, sessionId: String) {
        if (appStorage.userId.isEmpty()) {
            val action = GameDetailsFragmentDirections.actionGameDetailsFragmentToLoginFragment()
            findNavController().navigate(action)
        } else {
            val action = GameDetailsFragmentDirections.actionGameDetailsFragmentToGameFragment(
                id, gameurl = gameUrl, sessionId = sessionId
            )
            findNavController().navigate(action)
        }
    }

    override fun gameDetails(id: String) {

    }

    override fun statsDetails(id: String) {

    }

    override fun gameLoadfromPopup(gameUrl: String, entryFee: Int, gameId: Int, tournamentId: Int) {
        if (appStorage.userId.isEmpty()) {
            val action = GameDetailsFragmentDirections.actionGameDetailsFragmentToLoginFragment()
            findNavController().navigate(action)
        } else {
            sessionBuyInPopCall(gameUrl, gameId, entryFee, tournamentId)
        }
    }

    fun sessionBuyInPopCall(
        gameUrl: String,
        gameId: Int,
        entryFee: Int,
        tournamentId: Int,
    ) {
        val userId = appStorage.userId.toInt()
        mainViewModel.buyInPopUpDetails(
            requireContext(),
            gameId,
            entryFee,
            tournamentId,
            userId
        )
        mainViewModel.buyIn.observe(viewLifecycleOwner) {
            if (it.success) {
                mainViewModel.fetchSessionIdForGame(
                    context = requireActivity(),
                    gameId = gameId.toString(),
                    userId = appStorage.userId,
                    gameModeType = Constants.TOURNAMENT_MODE,
                    gameModeId = tournamentId.toString()
                )
                mainViewModel.sessionId.observe(viewLifecycleOwner) {
                    if (it.success) {
                        val action =
                            GameDetailsFragmentDirections.actionGameDetailsFragmentToGameFragment(
                                gameId.toString(),
                                gameUrl,
                                sessionId = it.data.sessionId
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun getGameData(position: Int, tournamentsItem: TournamentsItem) {

    }

    override fun getRecentGameUrl(position: Int, dataItem: DataItem) {
        var gameMode = ""
        gameMode = when (dataItem.category) {
            0 -> {
                Constants.FREE_PLAY_MODE
            }
            else -> {
                Constants.TOURNAMENT_MODE
            }
        }
        mainViewModel.fetchSessionIdForGame(
            context = requireActivity(),
            gameId = tournamentId,
            userId = appStorage.userId,
            gameModeType = gameMode,
            gameModeId = dataItem.id.toString()
        )
        mainViewModel.sessionId.observe(viewLifecycleOwner) {
            if (it.success) {
                val action = GameDetailsFragmentDirections.actionGameDetailsFragmentToGameFragment(
                    tournamentId,
                    gameurl = dataItem.gameUrl ?: "",
                    sessionId = it.data.sessionId
                )
                findNavController().navigate(action)
            }
        }
    }

    override fun buyInPopUp(
        gameId: Int,
        entryFee: Int,
        tournamentId: Int,
        listener: GameClickListener,
        url: String
    ) {
        if (appStorage.userId.isEmpty()) {
            AlertDialogUtil().showCustomAlert(
                requireContext(),
                listener,
                url,
                entryFee = entryFee,
                gameId,
                tournamentId
            )
        } else {
            val userId = appStorage.userId.toInt()
            mainViewModel.registerationDetails(requireContext(), gameId, tournamentId, userId)
            mainViewModel.registeration.observe(viewLifecycleOwner) {
                if (it.data.hasUserRegisteredTournament) {
                    mainViewModel.fetchSessionIdForGame(
                        context = requireActivity(),
                        gameId = gameId.toString(),
                        userId = appStorage.userId,
                        gameModeType = Constants.TOURNAMENT_MODE,
                        gameModeId = tournamentId.toString()
                    )
                    mainViewModel.sessionId.observe(viewLifecycleOwner) {
                        if (it.success) {
                            val action =
                                GameDetailsFragmentDirections.actionGameDetailsFragmentToGameFragment(
                                    gameId.toString(),
                                    gameUrl,
                                    sessionId = it.data.sessionId
                                )
                            findNavController().navigate(action)
                        }
                    }
                } else {
                    AlertDialogUtil().showCustomAlert(
                        requireContext(),
                        listener,
                        url,
                        entryFee = entryFee,
                        gameId,
                        tournamentId
                    )
                }
            }

        }
    }

    override fun getFreeGamePosition(position: Int, id: String) {
        mainViewModel.fetchSessionIdForGame(
            context = requireActivity(),
            gameId = tournamentId,
            userId = appStorage.userId,
            gameModeType = Constants.FREE_PLAY_MODE,
            gameModeId = id
        )
        mainViewModel.sessionId.observe(viewLifecycleOwner) {
            if (it.success) {
                val action = GameDetailsFragmentDirections.actionGameDetailsFragmentToGameFragment(
                    tournamentId,
                    gameurl = gameUrl,
                    sessionId = it.data.sessionId
                )
                findNavController().navigate(action)
            }
        }
    }

    override fun recentGamePosition(position: Int) {
        try {
            recentGameList.removeAt(position)
            binding.rvRecentGameList.apply {
                adapter?.notifyDataSetChanged()
            }
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    override fun getRecentStatsItemPosition(position: Int, dataItem: DataItem) {
        val action =
            GameDetailsFragmentDirections.actionGameDetailsFragmentToGameRecentRewardsAndLeaderFragment(
                tournamentsItem = dataItem,
                gameId = dataItem.id.toString(),
                tournamentId = tournamentId
            )
        findNavController().navigate(action)
    }


    override fun onViewStandingsClick(id: String, data: DataItem) {
        val action =
            GameDetailsFragmentDirections.actionGameDetailsFragmentToGameRecentRewardsAndLeaderFragment(
                data
            )
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()

    }


}
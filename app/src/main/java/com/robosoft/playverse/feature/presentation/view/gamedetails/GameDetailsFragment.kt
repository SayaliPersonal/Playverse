package com.robosoft.playverse.feature.presentation.view.gamedetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.playverse.data.models.DataItem
import com.playverse.data.models.TournamentsItem
import com.playverse.data.util.Constants
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.core.util.AlertDialogUtil
import com.robosoft.playverse.databinding.GameDetailsBinding
import com.robosoft.playverse.feature.presentation.view.home.BuyInClickListener
import com.robosoft.playverse.feature.presentation.view.profile.ServerDateTime
import com.robosoft.playverse.feature.presentation.viewModel.GameDetailsViewModel
import com.robosoft.playverse.utilities.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_get_started.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.game_details.*
import kotlinx.android.synthetic.main.game_info.view.*
import kotlinx.android.synthetic.main.name_title_layout.view.*
import javax.inject.Inject
import kotlin.math.abs


@AndroidEntryPoint
class GameDetailsFragment : Fragment(), TournamentItemRemoveListener, RecentItemRemoveListener,
    TournamentStatsClickListener, RecentGamesStatsClickListener, FreeGameClickListener,
    GameClickListener, TournamentGameClickListener, BuyInClickListener {

    private lateinit var binding: GameDetailsBinding
    private val mainViewModel: GameDetailsViewModel by viewModels()
    var tournamentsList: ArrayList<TournamentsItem> = ArrayList()
    var recentGameList: ArrayList<DataItem> = ArrayList()
    private val args: GameDetailsFragmentArgs by navArgs()
    private var gameUrl: String = ""

    private var isInitialSelection: Boolean = true


    @Inject
    lateinit var appStorage: AppStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* isInitialSelection = false*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GameDetailsBinding.inflate(inflater, container, false)
        subscribeUI()

        val time = ServerDateTime(requireActivity())
        time.fetch(object : ServerDateTime.CallBack {
            override fun onGetDateTime(date: String?, time: String?) {
                TemporaryStorage.date = date?.substring(0, 10) + " " + date?.substring(11, 19)
            }
        })

        binding.btnBack.setOnClickListener {
            val action = GameDetailsFragmentDirections.actionGameDetailsFragmentToHomeFragment()
            findNavController().navigate(action)
        }

        binding.swipeGameDetails.setOnRefreshListener {
            setUpData()
            swipe_game_details.isRefreshing = false
        }

        setUpData()

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visible(true)
            } else {
                binding.progressBar.visible(false)
            }
        }

        return binding.root
    }

    private fun setUpData() {
        TemporaryStorage.networkError.value = Connectivity.isNetworkAvailable(requireContext())
        if (TemporaryStorage.networkError.value == false) {
            binding.networkErrorValue = true
        }

        binding.networkError.btTryAgain.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        mainViewModel.getBannerAndOtherInfo(args.tournamentId)


        mainViewModel.res.observe(viewLifecycleOwner) {
            tournamentsList = it.data.tournaments
            gameUrl = it.data.gameUrl
            val adapter = GameDetailsViewPagerAdapter(it.data.banners)
            binding.imagesViewPager.adapter = adapter
            if (it.data.banners.isNotEmpty()) {
                binding.dotsIndicator.attachTo(binding.imagesViewPager)
                binding.imagesViewPager.autoScroll(3000)
            }
            binding.constraintPlayerInfo.textView.text = it.data.usersPlayed.toString()
            binding.constraintPlayerInfo.textView2.text = it.data.gamePlays.toString()
            binding.constraintPlayerInfo.textView3.text = it.data.rewardsWon.toString()

            Glide.with(this).load(it.data.iconUrl).into(
                binding.imgProfile
            )
            binding.gameInfoConstraint.txt_game_title.text = it.data.name
            binding.gameInfoConstraint.txt_developer_name.text = "by ${it.data.developer}"


        }
    }

    override fun tournamentPosition(position: Int) {

    }

    override fun recentGamePosition(position: Int) {

    }

    override fun getStatsItemPosition(position: Int, tournamentsItem: TournamentsItem) {
        val action =
            GameDetailsFragmentDirections.actionGameDetailsFragmentToGameRewardsAndLeaderFragment(
                tournamentsItem = tournamentsItem,
                gameId = tournamentsItem.id.toString(),
                gameUrl = gameUrl,
                tournamentId = args.tournamentId
            )
        findNavController().navigate(action)
    }

    override fun getRecentStatsItemPosition(position: Int, dataItem: DataItem) {
        val action =
            GameDetailsFragmentDirections.actionGameDetailsFragmentToGameRecentRewardsAndLeaderFragment(
                tournamentsItem = dataItem,
                gameId = dataItem.id.toString(),
                tournamentId = args.tournamentId
            )
        findNavController().navigate(action)
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
            gameId = args.tournamentId,
            userId = appStorage.userId,
            gameModeType = gameMode,
            gameModeId = dataItem.id.toString()
        )
        mainViewModel.sessionId.observe(viewLifecycleOwner) {
            if (it.success) {
                val action = GameDetailsFragmentDirections.actionGameDetailsFragmentToGameFragment(
                    args.tournamentId,
                    gameurl = dataItem.gameUrl ?: "",
                    sessionId = it.data.sessionId
                )
                findNavController().navigate(action)
            }
        }
    }

    override fun onViewStandingsClick(id: String, data: DataItem) {
        val action =
            GameDetailsFragmentDirections.actionGameDetailsFragmentToGameRecentRewardsAndLeaderFragment(
                data
            )
        findNavController().navigate(action)
    }


    override fun getFreeGamePosition(position: Int, id: String) {
        mainViewModel.fetchSessionIdForGame(
            context = requireActivity(),
            gameId = args.tournamentId,
            userId = appStorage.userId,
            gameModeType = Constants.FREE_PLAY_MODE,
            gameModeId = id
        )
        mainViewModel.sessionId.observe(viewLifecycleOwner) {
            if (it.success) {
                val action = GameDetailsFragmentDirections.actionGameDetailsFragmentToGameFragment(
                    args.tournamentId,
                    gameurl = gameUrl,
                    sessionId = it.data.sessionId
                )
                findNavController().navigate(action)
            }
        }
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
                        gameId = args.tournamentId,
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
                    gameId = args.tournamentId,
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


    override fun gameDetails(id: String) {
        // do nothing
    }

    override fun statsDetails(id: String) {
        // do nothing
    }

    override fun gameLoadfromPopup(gameUrl: String, entryFee: Int, gameId: Int, tournamentId: Int) {
        if (appStorage.userId.isEmpty()) {
            val action = GameDetailsFragmentDirections.actionGameDetailsFragmentToLoginFragment()
            findNavController().navigate(action)
        } else {
            sessionBuyInPopCall(gameUrl, gameId, entryFee, tournamentId)
        }
    }

    override fun getGameData(position: Int, tournamentsItem: TournamentsItem) {
        // do nothing
    }

    override fun onResume() {
        super.onResume()
        if (view == null) {
            return
        }
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                val action = GameDetailsFragmentDirections.actionGameDetailsFragmentToHomeFragment()
                findNavController().navigate(action)
                true
            } else false
        }
    }

    private fun subscribeUI() {

        val adapter = GameDetailsTabAdapter(requireActivity(), args.tournamentId)
        binding.vpGameDetails.adapter = adapter
        binding.vpGameDetails.isUserInputEnabled = false
        binding.vpGameDetails.let {
            TabLayoutMediator(binding.tabGameDetails, it) { tab, position ->
                if (position == 0)
                    tab.text = getString(R.string.game_modes)
                else if (position == 1)
                    tab.text = getString(R.string.hall_of_fame)
            }.attach()
        }

        /*setupViewPager()*/

        for (i in 0 until binding.tabGameDetails.tabCount) {
            val tab = (binding.tabGameDetails.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(10, 10, 10, 10)
            tab.requestLayout()
        }

        binding.vpGameDetails.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.vpGameDetails.post(Runnable { binding.vpGameDetails.requestLayout() })
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                binding.vpGameDetails.post(Runnable { binding.vpGameDetails.requestLayout() })
            }
        })
    }


}





package com.robosoft.playverse.feature.presentation.view.gamedetails

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.playverse.data.models.LeaderboardItem
import com.playverse.data.models.RewardsItem
import com.playverse.data.models.TournamentsItem
import com.playverse.data.util.Constants
import com.robosoft.playverse.MainActivity
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.core.util.AlertDialogUtil
import com.robosoft.playverse.databinding.FragmentGameRewardsAndLeaderBinding
import com.robosoft.playverse.feature.presentation.view.home.BuyInClickListener
import com.robosoft.playverse.feature.presentation.view.home.HomeFragmentDirections
import com.robosoft.playverse.feature.presentation.view.profile.ServerDateTime
import com.robosoft.playverse.feature.presentation.viewModel.GameRewardsAndLeaderBoardViewModel
import com.robosoft.playverse.feature.presentation.viewModel.TournamentViewModel
import com.robosoft.playverse.utilities.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.rank_list.view.*
import kotlinx.android.synthetic.main.rewards_card_view.view.*
import kotlinx.android.synthetic.main.rewards_stand_leaderboard.view.*
import kotlinx.android.synthetic.main.text_cross_button.view.*
import kotlinx.android.synthetic.main.tournamant.view.*
import kotlinx.android.synthetic.main.tournamant.view.txt_rank
import kotlinx.android.synthetic.main.tournamant_game_info_new.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class GameRewardsAndLeaderFragment : Fragment(), GameClickListener, BuyInClickListener {

    private lateinit var binding: FragmentGameRewardsAndLeaderBinding
    private val viewModel: GameRewardsAndLeaderBoardViewModel by viewModels()
    private var leaderboardTop: List<LeaderboardItem> = ArrayList()
    private val args: GameRewardsAndLeaderFragmentArgs by navArgs()
    private var timer = Timer()
    private lateinit var rewards: List<RewardsItem>

    @Inject
    lateinit var appStorage: AppStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameRewardsAndLeaderBinding.inflate(inflater, container, false)
        binding.constraintTransparent.rvRankList.layoutManager =
            LinearLayoutManager(requireContext())
        binding.rewardsConstraint.rvRankAmountList.layoutManager =
            GridLayoutManager(requireContext(), 4)

        val time = ServerDateTime(requireActivity())
        time.fetch(object : ServerDateTime.CallBack {
            override fun onGetDateTime(date: String?, time: String?) {
                TemporaryStorage.date = date?.substring(0, 10) + " " + date?.substring(11, 19)
            }
        })

        TemporaryStorage.networkError.value = Connectivity.isNetworkAvailable(requireContext())
        if(TemporaryStorage.networkError.value == false){
            binding.networkErrorValue = true
        }

        binding.networkError.btTryAgain.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }


        updateTournamentUI(args.tournamentsItem)

        viewModel.getRewardsAndLeaderBoard(args.gameId)

        viewModel.rewards.observe(viewLifecycleOwner) {
            this.rewards = it.data.rewards
            if (it.data.rewards.isNotEmpty()) {
                binding.rewardsConstraint.visible(true)
                if (it.data.rewards.size > 8) {
                    binding.rewardsConstraint.rvRankAmountList.adapter =
                        RankAmountListAdapter(it.data.rewards.subList(0, 8))
                    binding.rewardsConstraint.downArrow.visibleOrInvisible(true)
                } else {
                    binding.rewardsConstraint.rvRankAmountList.adapter =
                        RankAmountListAdapter(it.data.rewards)
                    binding.rewardsConstraint.downArrow.visibleOrInvisible(false)
                }
            } else {
                binding.rewardsConstraint.visible(false)
            }

            if (it.data.leaderboard.isNotEmpty()) {
                binding.txtCurrentStanding.visible(true)
                binding.constraintTransparent.visible(true)
                try {
                    when (it.data.leaderboard.size) {
                        1 -> {

                            if (it.data.leaderboard[0].rank == 1) {
                                binding.constraintTransparent.txt_winner_one.text =
                                    getString(R.string._1st)
                            }

                            binding.constraintTransparent.txt_winner_one_name.text =
                                it.data.leaderboard[0].userName

                            binding.constraintTransparent.txt_winner_one_count.text =
                                convertCurrency(it.data.leaderboard[0].score)

                            Glide.with(this).load(it.data.leaderboard[0].profileUrl).into(
                                binding.constraintTransparent.img_first
                            )
                        }
                        2 -> {
                            binding.constraintTransparent.txt_winner_one_name.text =
                                it.data.leaderboard[0].userName
                            binding.constraintTransparent.txt_winner_one_count.text =
                                convertCurrency(it.data.leaderboard[0].score)

                            binding.constraintTransparent.txt_winner_two_name.text =
                                it.data.leaderboard[1].userName
                            binding.constraintTransparent.txt_winner_two_count.text =
                                convertCurrency(it.data.leaderboard[1].score)

                            Glide.with(this).load(it.data.leaderboard[0].profileUrl).into(
                                binding.constraintTransparent.img_first
                            )
                            Glide.with(this).load(it.data.leaderboard[1].profileUrl).into(
                                binding.constraintTransparent.img_second
                            )

                            if (it.data.leaderboard[0].rank == 1) {
                                binding.constraintTransparent.txt_winner_one.text =
                                    getString(R.string._1st)
                            }

                            if (it.data.leaderboard[1].rank == 1) {
                                binding.constraintTransparent.txt_winner_two.text =
                                    getString(R.string._1st)
                            }

                            if (it.data.leaderboard[1].rank == 2) {
                                binding.constraintTransparent.txt_winner_two.text =
                                    getString(R.string._2nd)
                            }

                        }
                        3 -> {

                            if (it.data.leaderboard[0].rank == 1) {
                                binding.constraintTransparent.txt_winner_one.text =
                                    getString(R.string._1st)
                            }

                            if (it.data.leaderboard[1].rank == 1) {
                                binding.constraintTransparent.txt_winner_two.text =
                                    getString(R.string._1st)
                            }

                            if (it.data.leaderboard[2].rank == 1) {
                                binding.constraintTransparent.txt_winner_three.text =
                                    getString(R.string._1st)
                            }

                            if (it.data.leaderboard[1].rank == 2) {
                                binding.constraintTransparent.txt_winner_two.text =
                                    getString(R.string._2nd)
                            }

                            if (it.data.leaderboard[2].rank == 2) {
                                binding.constraintTransparent.txt_winner_three.text =
                                    getString(R.string._2nd)
                            }

                            if (it.data.leaderboard[2].rank == 3) {
                                binding.constraintTransparent.txt_winner_three.text =
                                    getString(R.string._3rd)
                            }

                            binding.constraintTransparent.txt_winner_one_name.text =
                                it.data.leaderboard[0].userName
                            binding.constraintTransparent.txt_winner_one_count.text =
                                convertCurrency(it.data.leaderboard[0].score)

                            binding.constraintTransparent.txt_winner_two_name.text =
                                it.data.leaderboard[1].userName

                            binding.constraintTransparent.txt_winner_two_count.text =
                                convertCurrency(it.data.leaderboard[1].score)

                            binding.constraintTransparent.txt_winner_three_name.text =
                                it.data.leaderboard[2].userName

                            binding.constraintTransparent.txt_winner_three_count.text =
                                convertCurrency(it.data.leaderboard[2].score)

                            Glide.with(this).load(it.data.leaderboard[0].profileUrl).into(
                                binding.constraintTransparent.img_first
                            )
                            Glide.with(this).load(it.data.leaderboard[1].profileUrl).into(
                                binding.constraintTransparent.img_second
                            )

                            Glide.with(this).load(it.data.leaderboard[2].profileUrl).into(
                                binding.constraintTransparent.img_third
                            )
                        }
                        else -> {
                            leaderboardTop = it.data.leaderboard.subList(0, 3)

                            binding.constraintTransparent.rvRankList.adapter =

                                RankListAdapter(
                                    it.data.leaderboard.subList(
                                        3,
                                        it.data.leaderboard.size
                                    ), userId = appStorage.userId
                                )

                            if (leaderboardTop[0].rank == 1) {
                                binding.constraintTransparent.txt_winner_one.text =
                                    getString(R.string._1st)
                            }

                            if (leaderboardTop[1].rank == 1) {
                                binding.constraintTransparent.txt_winner_two.text =
                                    getString(R.string._1st)
                            }

                            if (leaderboardTop[2].rank == 1) {
                                binding.constraintTransparent.txt_winner_three.text =
                                    getString(R.string._1st)
                            }

                            if (leaderboardTop[1].rank == 2) {
                                binding.constraintTransparent.txt_winner_two.text =
                                    getString(R.string._2nd)
                            }

                            if (leaderboardTop[2].rank == 2) {
                                binding.constraintTransparent.txt_winner_three.text =
                                    getString(R.string._2nd)
                            }

                            if (leaderboardTop[2].rank == 3) {
                                binding.constraintTransparent.txt_winner_three.text =
                                    getString(R.string._3rd)
                            }

                            binding.constraintTransparent.txt_winner_one_name.text =
                                leaderboardTop[0].userName

                            binding.constraintTransparent.txt_winner_one_count.text =
                                convertCurrency(leaderboardTop[0].score)

                            binding.constraintTransparent.txt_winner_two_name.text =
                                leaderboardTop[1].userName
                            binding.constraintTransparent.txt_winner_two_count.text =
                                convertCurrency(leaderboardTop[1].score)

                            binding.constraintTransparent.txt_winner_three_name.text =
                                leaderboardTop[2].userName
                            binding.constraintTransparent.txt_winner_three_count.text =
                                convertCurrency(leaderboardTop[2].score)

                            Glide.with(this).load(leaderboardTop[0].profileUrl).into(
                                binding.constraintTransparent.img_first
                            )
                            Glide.with(this).load(leaderboardTop[1].profileUrl).into(
                                binding.constraintTransparent.img_second
                            )

                            Glide.with(this).load(leaderboardTop[2].profileUrl).into(
                                binding.constraintTransparent.img_third
                            )
                        }
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                binding.txtCurrentStanding.visible(false)
                binding.constraintTransparent.visible(false)
            }
        }

        binding.constraintTitleHeader.ivClose.setOnClickListener {
            val action =
                GameRewardsAndLeaderFragmentDirections.actionGameRewardsAndLeaderFragmentToGameDetailsFragment(
                    args.tournamentId
                )
            findNavController().navigate(action)
        }

        binding.rewardsConstraint.downArrow.setOnClickListener {
            if (::rewards.isInitialized) {
                binding.rewardsConstraint.rvRankAmountList.adapter =
                    RankAmountListAdapter(rewards)
                binding.rewardsConstraint.downArrow.visibleOrInvisible(false)
            }
        }

        return binding.root
    }

    private fun updateTournamentUI(tournamentsItem: TournamentsItem) {
        binding.tournamentConstraint.apply {
            val paint = tvWinAmt.paint
            val width = paint.measureText(textView.text.toString())
            val textShader: Shader = LinearGradient(
                0f, 0f, width, textView.textSize, intArrayOf(
                    Color.parseColor("#FFBA00"),
                    Color.parseColor("#FFCA3A"),
                    Color.parseColor("#FFE597"),
                    Color.parseColor("#FFCA3A"),
                    Color.parseColor("#D96201")
                ), null, Shader.TileMode.REPEAT
            )
            tvWinAmt.paint.shader = textShader
            tvWinAmt.text = tournamentsItem.totalRewards.toString()
            textView2.text = tournamentsItem.players.toString()
            textView.text = tournamentsItem.freeGamePlays.toString()
            txt_rank.text =
                "${context.getString(R.string.guaranteed_rewards_up_to_rank)} ${tournamentsItem.rank}"
            constraint_tournamant_playerInfo.btnEntryFee.text =
                "${context.getString(R.string.play_for_20)} ₹${tournamentsItem.entryFee}"
        }
        startTimer(
            tournamentsItem.startTime,
            tournamentsItem.endTime
        )
        when (tournamentsItem.category) {
            1 -> {
                binding.tournamentConstraint.txt_tournament.apply {
                    background =
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.button_border,
                            null
                        )
                    text = context.getString(R.string.low_stake_tournament)

                }
                binding.tournamentConstraint.btnEntryFee.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_btn_bg_blue, null)
                binding.tournamentConstraint.img_prize.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_cup,
                        null
                    )
                )
            }
            2 -> {
                binding.tournamentConstraint.txt_tournament.apply {
                    background =
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.purple_border,
                            null
                        )
                    text = context.getString(R.string.mid_stake_tournament)
                }
                binding.tournamentConstraint.btnEntryFee.background =
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_btn_bg_purple,
                        null
                    )
                binding.tournamentConstraint.img_prize.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_cup_purple,
                        null
                    )
                )
            }
            3 -> {
                binding.tournamentConstraint.txt_tournament.apply {
                    background =
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.golden_border,
                            null
                        )
                    text = context.getString(R.string.high_stake_tournament)
                }
                binding.tournamentConstraint.btnEntryFee.background =
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_btn_bg_golden,
                        null
                    )
                binding.tournamentConstraint.img_prize.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_cup_golden,
                        null
                    )
                )
            }
        }

        binding.tournamentConstraint.constraint_tournamant_playerInfo.btnEntryFee.setOnClickListener {
            this.buyInPopUp(
                args.gameId.toInt(),
                tournamentsItem.entryFee.toInt(),
                args.tournamentId.toInt(),
                this,
                args.gameUrl
            )
        }
    }

    private fun startTimer(
        startDate: String,
        endDate: String
    ) {
        val timer = object :
            CountDownTimer(
                getTotalMillis(startDate = TemporaryStorage.date, endDate = endDate),
                1000
            ) {
            override fun onTick(millisUntilFinished: Long) {
                var time = millisUntilFinished
                val d: Long = TimeUnit.MILLISECONDS.toDays(time)
                time -= TimeUnit.DAYS.toMillis(d)

                val h: Long = TimeUnit.MILLISECONDS.toHours(time)
                time -= TimeUnit.HOURS.toMillis(h)

                val m: Long = TimeUnit.MILLISECONDS.toMinutes(time)
                time -= TimeUnit.MINUTES.toMillis(m)

                val s: Long = TimeUnit.MILLISECONDS.toSeconds(time)

                binding.tournamentConstraint.txt_time.text =
                    "${d}d: ${h}h: ${m}m: $s" //You can compute the millisUntilFinished on hours/minutes/seconds

                if (d == 0L && s == 0L && m == 0L && h == 0L) {
                    timer.cancel()
                }
            }

            override fun onFinish() {
                timer.cancel()
            }
        }
        timer.start()
    }

    override fun gameLoad(id: String, gameUrl: String, sessionId: String) {
        //do nothing
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
        }else{
            val userId = appStorage.userId.toInt()
            viewModel.registerationDetails(requireContext(),tournamentId, gameId, userId)
            viewModel.registeration.observe(viewLifecycleOwner) {
                if (it.data.hasUserRegisteredTournament) {
                    viewModel.fetchSessionIdForGame(
                        context = requireActivity(),
                        gameId = args.tournamentId,
                        userId = appStorage.userId,
                        gameModeType = Constants.TOURNAMENT_MODE,
                        gameModeId = args.gameId
                    )
                    viewModel.sessionId.observe(viewLifecycleOwner) {
                        if (it.success) {
                            val action =
                                GameRewardsAndLeaderFragmentDirections.actionGameRewardsAndLeaderFragmentToGameFragment(
                                    args.tournamentId,
                                    url,
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
        viewModel.buyInPopUpDetails(requireContext(), tournamentId, entryFee, gameId, userId)
        viewModel.buyIn.observe(viewLifecycleOwner) {
            if (it.success) {
                viewModel.fetchSessionIdForGame(
                    context = requireActivity(),
                    gameId = args.tournamentId,
                    userId = appStorage.userId,
                    gameModeType = Constants.TOURNAMENT_MODE,
                    gameModeId = args.gameId
                )
                viewModel.sessionId.observe(viewLifecycleOwner) {
                    if (it.success) {
                        val action =
                            GameRewardsAndLeaderFragmentDirections.actionGameRewardsAndLeaderFragmentToGameFragment(
                                args.tournamentId,
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

    }

    override fun statsDetails(id: String) {

    }

    override fun gameLoadfromPopup(gameUrl: String, entryFee: Int, gameId: Int, tournamentId: Int) {
        if (appStorage.userId.isEmpty()) {
            val action =
                GameRewardsAndLeaderFragmentDirections.actionGameRewardsAndLeaderFragmentToLoginFragment()
            findNavController().navigate(action)
        } else {
            sessionBuyInPopCall(gameUrl, gameId, entryFee, tournamentId);

        }

    }

}
package com.robosoft.playverse.feature.presentation.view.gamedetails

import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.playverse.data.models.DataItem
import com.playverse.data.models.LeaderboardItem
import com.playverse.data.models.RewardsItem
import com.playverse.data.util.Constants
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentRecentGameRewardTournamentBinding
import com.robosoft.playverse.feature.presentation.view.profile.ServerDateTime
import com.robosoft.playverse.feature.presentation.viewModel.GameRewardsAndLeaderBoardViewModel
import com.robosoft.playverse.utilities.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.recent_games.view.*
import kotlinx.android.synthetic.main.recent_games_check.view.constraint_card
import kotlinx.android.synthetic.main.recent_games_check.view.inner_card
import kotlinx.android.synthetic.main.recent_games_check.view.*
import kotlinx.android.synthetic.main.rewards_card_view.view.*
import kotlinx.android.synthetic.main.rewards_stand_leaderboard.view.*
import kotlinx.android.synthetic.main.text_cross_button.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class RecentGameRewardTournament : Fragment() {

    private lateinit var binding: FragmentRecentGameRewardTournamentBinding
    private val viewModel: GameRewardsAndLeaderBoardViewModel by viewModels()
    private var leaderboardTop: List<LeaderboardItem> = ArrayList()
    private val args: RecentGameRewardTournamentArgs by navArgs()
    private var timer = Timer()
    private lateinit var rewards: List<RewardsItem>

    @Inject
    lateinit var appStorage: AppStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecentGameRewardTournamentBinding.inflate(inflater, container, false)
        binding.constraintTransparent.rvRankList.layoutManager =
            LinearLayoutManager(requireContext())
        binding.rewardsConstraint.rvRankAmountList.layoutManager =
            GridLayoutManager(requireContext(), 4)

        val time = ServerDateTime(requireActivity())
        time.fetch(object : ServerDateTime.CallBack {
            override fun onGetDateTime(date: String?, time: String?) {
                TemporaryStorage.date = date?.substring(0,10) + " " +  date?.substring(11,19)
            }
        })

        TemporaryStorage.networkError.value = Connectivity.isNetworkAvailable(requireContext())
        if(TemporaryStorage.networkError.value == false){
            binding.networkErrorValue = true
        }

        binding.networkError.btTryAgain.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }


        updateRecentTournamentUI(args.tournamentsItem)

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

                            Glide.with(this).load(it.data.leaderboard[0].profileUrl).into(
                                binding.constraintTransparent.img_first
                            )
                            Glide.with(this).load(it.data.leaderboard[1].profileUrl).into(
                                binding.constraintTransparent.img_second
                            )
                        }
                        3 -> {
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

                            if (leaderboardTop[2].rank == 3) {
                                binding.constraintTransparent.txt_winner_three.text =
                                    getString(R.string._3rd)
                            }

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
                                        it.data.leaderboard.size,
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
            if (args.fromProfileTournaments) {
                findNavController().navigate(R.id.profileDetailsFragment)
            } else {
                val action =
                    RecentGameRewardTournamentDirections.actionGameRecentRewardsAndLeaderFragmentToGameDetailsFragment(
                        args.tournamentId
                    )
                findNavController().navigate(action)
            }
        }

        binding.rewardsConstraint.downArrow.setOnClickListener {
            if (::rewards.isInitialized) {
                binding.rewardsConstraint.rvRankAmountList.adapter =
                    RankAmountListAdapter(rewards)
                binding.rewardsConstraint.downArrow.visibleOrInvisible(false)
            }
        }

        if (args.viewStandingsClick) binding.recentGamesCheck.isGone = true


        return binding.root
    }

    private fun updateRecentTournamentUI(tournamentsItem: DataItem) {

        binding.recentGameConstraint.apply {
            your_score.text = tournamentsItem.userScore.toString()
            your_rankk.text = tournamentsItem.userRank.toString()
            your_hieghst_scoree.text = tournamentsItem.currentHighScore.toString()
            if (tournamentsItem.userRank == 1) {
                your_score_detaill.text =
                    context.getString(R.string.congrats_you_have_the_highest_score)
            } else {
                your_score_detaill.text =
                    "Just ${tournamentsItem.targetScore} points more to win  ₹${tournamentsItem?.targetReward}"
            }
        }

        startTimer(
            tournamentsItem.startTime,
            tournamentsItem.endTime,
            tournamentsItem
        )

        when (tournamentsItem.category) {
            1 -> {
                binding.recentGameConstraint.apply {
                    constraint_card.background =
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.button_border,
                            null
                        )


                    inner_card.setCardBackgroundColor(
                        requireContext().getColor(
                            R.color.bluecolor
                        )
                    )
                }
            }
            2 -> {
                binding.recentGameConstraint.apply {
                    constraint_card.background =
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.purple_card_border,
                            null
                        )
                    txt_recent_tournament.apply {
                        text = context.getString(R.string.mid_stake_tournament)
                        background =
                            ResourcesCompat.getDrawable(
                                requireContext().resources,
                                R.drawable.purple_card_border,
                                null
                            )
                    }
                    inner_card.setCardBackgroundColor(
                        requireContext().getColor(
                            R.color.purplecolor
                        )
                    )

                }
            }
            3 -> {
                binding.recentGameConstraint.apply {
                    constraint_card.background =
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.golden_card_border,
                            null
                        )
                    txt_recent_tournament.apply {
                        text = context.getString(R.string.high_stake_tournament)
                        background =
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.golden_card_border,
                                null
                            )
                    }
                    inner_card.background =
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.golden_card_border,
                            null
                        )
                }
            }
        }
        binding.recentGameConstraint.btn_recent_continue.setOnClickListener {
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
                        RecentGameRewardTournamentDirections.actionGameRecentRewardsAndLeaderFragmentToGameFragment(
                            args.tournamentId,
                            gameurl = tournamentsItem.gameUrl ?: args.tournamentsItem.gameUrl ?: "",
                            sessionId = it.data.sessionId
                        )
                    findNavController().navigate(action)
                }

            }
        }
    }

    private fun startTimer(
        startDate: String,
        endDate: String,
        tournamentsItem: DataItem
    ) {
        val timer = object :
            CountDownTimer(getTotalMillis(startDate = TemporaryStorage.date, endDate = endDate), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var time = millisUntilFinished
                val d: Long = TimeUnit.MILLISECONDS.toDays(time)
                time -= TimeUnit.DAYS.toMillis(d)

                val h: Long = TimeUnit.MILLISECONDS.toHours(time)
                time -= TimeUnit.HOURS.toMillis(h)

                val m: Long = TimeUnit.MILLISECONDS.toMinutes(time)
                time -= TimeUnit.MINUTES.toMillis(m)

                val s: Long = TimeUnit.MILLISECONDS.toSeconds(time)

                binding.recentGameConstraint.txt_time_recentt.text =
                    "${d}d: ${h}h: ${m}m: $s" //You can compute the millisUntilFinished on hours/minutes/seconds

                if (d == 0L && s == 0L && m == 0L && h == 0L) {
                    timer.cancel()
                    binding.recentGamesCheck.txt_time_recentt.text = "Tournament ended"
                    binding.recentGamesCheck.your_score_detaill.isGone = true
                    binding.recentGamesCheck.btn_recent_continue.isGone = true
                    binding.recentGamesCheck.img_game_statss.isGone = true
                    binding.recentGamesCheck.btn_standings.isVisible = true
                    binding.recentGamesCheck.your_hieghst_score_titlee.text = "Highest score"
                    binding.recentGamesCheck.your_rankk.setTextSize(TypedValue.COMPLEX_UNIT_SP,40F);
                    binding.recentGamesCheck.txt_time_recentt.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)

                    binding.recentGamesCheck.btn_standings.setOnClickListener {
                        val action = RecentGameRewardTournamentDirections.actionGameRecentRewardsAndLeaderFragmentToGameRecentRewardsAndLeaderFragment(gameId = tournamentsItem.id.toString() , tournamentsItem =  tournamentsItem , viewStandingsClick = true)
                        findNavController().navigate(action)
                    }
                }
            }

            override fun onFinish() {
                timer.cancel()
            }
        }
        timer.start()
    }

}
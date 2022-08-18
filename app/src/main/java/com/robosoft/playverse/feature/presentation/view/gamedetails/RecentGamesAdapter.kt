package com.robosoft.playverse.feature.presentation.view.gamedetails

import android.content.Context
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.playverse.data.models.DataItem
import com.robosoft.playverse.R
import com.robosoft.playverse.utilities.TemporaryStorage
import com.robosoft.playverse.utilities.getTotalMillis
import com.robosoft.playverse.utilities.visible
import com.robosoft.playverse.utilities.visibleOrInvisible
import kotlinx.android.synthetic.main.item_profile_tournaments.view.*
import kotlinx.android.synthetic.main.recent_games.view.*
import kotlinx.android.synthetic.main.tournamant.view.txt_tournament
import java.util.*
import java.util.concurrent.TimeUnit

class RecentGamesAdapter(
    private val dataList: List<DataItem>,
    private val recentItemRemoveListener: RecentItemRemoveListener,
    private val recentGamesStatsClickListener: RecentGamesStatsClickListener
) :
    RecyclerView.Adapter<RecentGamesAdapter.RecentGameViewHolder>() {

    lateinit var context: Context
    private var timer = Timer()

    class RecentGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentGameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recent_games, parent, false)
        context = parent.context

        return RecentGameViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: RecentGameViewHolder, position: Int) {

        holder.itemView.apply {
            your_scoce.text = dataList[position].userScore.toString()
            your_rank.text = dataList[position].userRank.toString()
            your_hieghst_score.text = dataList[position].currentHighScore.toString()
            if (dataList[position].category == 0) {
                if (dataList[position].targetScore!! <= dataList[position].userScore) {
                    your_score_detail.text =
                        "Congrats! You won  ₹${dataList[position]?.targetReward}"
                } else {
                    your_score_detail.text =
                        "Just ${dataList[position].targetScore} points more to win  ₹${dataList[position]?.targetReward}"
                }
            } else {
                if (dataList[position].userRank == 1) {
                    your_score_detail.text =
                        context.getString(R.string.congrats_you_have_the_highest_score)
                } else {
                    your_score_detail.text =
                        "Just ${dataList[position].targetScore} points more to win  ₹${dataList[position]?.targetReward}"
                }
            }

            img_game_stats.setOnClickListener {
                recentGamesStatsClickListener.getRecentStatsItemPosition(
                    position = position,
                    dataList[position]
                )
            }

            btn_continue.setOnClickListener {
                recentGamesStatsClickListener.getRecentGameUrl(
                    position = position,
                    dataList[position]
                )
            }
        }

        if (dataList[position].category != 0) {
            startTimer(
                holder,
                position,
                dataList[position].startTime,
                dataList[position].endTime,
                dataList
            )
        }

        when (dataList[position].category) {
            0 -> {
                holder.itemView.apply {
                    txt_time_recent.visibleOrInvisible(visible = false)
                    img_game_stats.visibleOrInvisible(visible = false)
                    your_hieghst_score.visibleOrInvisible(visible = false)
                    your_hieghst_score_title.visibleOrInvisible(visible = false)
                    your_rank_title.visible(visible = false)
                    your_rank.visible(visible = false)
                    img_astronut.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.ic_free_avatar,
                            null
                        )
                    )
                    constraint_card.background =
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.button_border,
                            null
                        )
                    txt_tournament.apply {
                        text = context.getString(R.string.free_game_play)
                        background =
                            ResourcesCompat.getDrawable(
                                context.resources,
                                R.drawable.green_gradient,
                                null
                            )
                    }
                    inner_card.setCardBackgroundColor(
                        context.getColor(
                            R.color.bluecolor
                        )
                    )
                }
            }
            1 -> {
                holder.itemView.apply {
                    constraint_card.background =
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.button_border,
                            null
                        )
                    txt_tournament.text = context.getString(R.string.low_stake_tournament)

                    inner_card.setCardBackgroundColor(
                        context.getColor(
                            R.color.bluecolor
                        )
                    )
                }
            }
            2 -> {
                holder.itemView.apply {
                    constraint_card.background =
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.purple_card_border,
                            null
                        )
                    txt_tournament.apply {
                        text = context.getString(R.string.mid_stake_tournament)
                        background =
                            ResourcesCompat.getDrawable(
                                context.resources,
                                R.drawable.purple_card_border,
                                null
                            )
                    }
                    inner_card.setCardBackgroundColor(
                        context.getColor(
                            R.color.purplecolor
                        )
                    )

                }
            }
            3 -> {
                holder.itemView.apply {
                    constraint_card.background =
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.golden_card_border,
                            null
                        )
                    txt_tournament.apply {
                        text = context.getString(R.string.high_stake_tournament)
                        background =
                            ResourcesCompat.getDrawable(
                                context.resources,
                                R.drawable.golden_card_border,
                                null
                            )
                    }
                    inner_card.background =
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.golden_card_border,
                            null
                        )
                }
            }
        }


    }

    private fun startTimer(
        holder: RecentGameViewHolder,
        position: Int,
        startDate: String,
        endDate: String,
        dataList: List<DataItem>
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

                holder.itemView.txt_time_recent.text =
                    "${d}d: ${h}h: ${m}m: $s" //You can compute the millisUntilFinished on hours/minutes/seconds

                if (d == 0L && s == 0L && m == 0L && h == 0L) {
                    timer.cancel()
//                    recentItemRemoveListener.recentGamePosition(position)
                    holder.itemView.txt_time_recent.text = "Tournament ended"
                    holder.itemView.your_score_detail.isGone = true
                    holder.itemView.btn_continue.isGone = true
                    holder.itemView.img_game_stats.isGone = true
                    holder.itemView.btn_view_standings.isVisible = true
                    holder.itemView.your_hieghst_score_title.text = "Highest score"
                    holder.itemView.your_rank.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40F);
                    holder.itemView.txt_time_recent.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        null,
                        null
                    )

                    holder.itemView.btn_view_standings.setOnClickListener {
                        recentGamesStatsClickListener.onViewStandingsClick(
                            dataList[position].id.toString(),
                            dataList[position]
                        )
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
package com.robosoft.playverse.feature.presentation.view.gamedetails

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.playverse.data.models.TournamentsItem
import com.robosoft.playverse.R
import com.robosoft.playverse.feature.presentation.view.home.BuyInClickListener
import com.robosoft.playverse.utilities.TemporaryStorage
import com.robosoft.playverse.utilities.convertCurrency
import com.robosoft.playverse.utilities.getTotalMillis
import kotlinx.android.synthetic.main.tournamant.view.*
import kotlinx.android.synthetic.main.tournamant_game_info.view.*
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by Subham on 7/13/2022.
 */
class TournamentGameListAdapter(
    private var tournaments: ArrayList<TournamentsItem>,
    private var tournamentItemRemoveListener: TournamentItemRemoveListener,
    private var tournamentStatsClickListener: TournamentStatsClickListener,
    private var gameClickListener: GameClickListener,
    private var buyInClickListener: BuyInClickListener,
    private var tournamentGameClickListener: TournamentGameClickListener,
    private var gameUrl: String,
    private var gameId: String
) :
    RecyclerView.Adapter<TournamentGameListAdapter.TournamentViewHolder>() {

    lateinit var context: Context
    private var timer = Timer()

    class TournamentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tournamant, parent, false)
        context = parent.context

        return TournamentViewHolder(view)
    }

    override fun getItemCount(): Int = tournaments.size

    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
        /*  1 - Low Stake 2 - Mid Stake 3 - High Stake*/

        holder.itemView.apply {
            tvWinAmt.text = tournaments[position].totalRewards.toString()
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
            textView2.text = convertCurrency(tournaments[position].players)
            textView.text = convertCurrency(tournaments[position].freeGamePlays)
            startTimer(
                holder,
                position,
                tournaments[position].startTime,
                tournaments[position].endTime
            )
            // constraint_card_parent.alpha = 0.7F
            txt_rank.text =
                "${context.getString(R.string.guaranteed_rewards_up_to_rank)} ${tournaments[position].rank}"
            constraint_tournamant_playerInfo.btnEntryFee.text =
                "${context.getString(R.string.play_for_20)} ₹${tournaments[position].entryFee}"

            img_game_won.setOnClickListener {
                tournamentStatsClickListener.getStatsItemPosition(
                    position = position,
                    tournaments[position]
                )
            }

            btnEntryFee.setOnClickListener {
                buyInClickListener.buyInPopUp(
                    gameId = gameId.toInt(),
                    entryFee = tournaments[position].entryFee.toInt(),
                    tournamentId = tournaments[position].id,
                    listener = gameClickListener,
                    url = gameUrl
                )
            }
        }
        when (tournaments[position].category) {
            1 -> {
                holder.itemView.txt_tournament.apply {
                    background =
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.button_border,
                            null
                        )
                    text = context.getString(R.string.low_stake_tournament)

                }
                holder.itemView.btnEntryFee.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.ic_btn_bg_blue, null)
                holder.itemView.img_prize.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_cup,
                        null
                    )
                )
            }
            2 -> {
                holder.itemView.txt_tournament.apply {
                    background =
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.purple_border,
                            null
                        )
                    text = context.getString(R.string.mid_stake_tournament)
                }
                holder.itemView.btnEntryFee.background =
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_btn_bg_purple,
                        null
                    )
                holder.itemView.img_prize.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_cup_purple,
                        null
                    )
                )
            }
            3 -> {
                holder.itemView.txt_tournament.apply {
                    background =
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.golden_border,
                            null
                        )
                    text = context.getString(R.string.high_stake_tournament)
                }
                holder.itemView.btnEntryFee.background =
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_btn_bg_golden,
                        null
                    )
                holder.itemView.img_prize.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_cup_golden,
                        null
                    )
                )
            }
        }
    }

    private fun startTimer(
        holder: TournamentViewHolder,
        position: Int,
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

                holder.itemView.txt_time.text =
                    "${d}d: ${h}h: ${m}m: $s" //You can compute the millisUntilFinished on hours/minutes/seconds

                if (d == 0L && s == 0L && m == 0L && h == 0L) {
                    timer.cancel()
                    tournamentItemRemoveListener.tournamentPosition(position)
                }
            }

            override fun onFinish() {
                timer.cancel()
            }
        }
        timer.start()
    }
}
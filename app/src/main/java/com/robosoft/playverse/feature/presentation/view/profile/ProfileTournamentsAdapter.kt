package com.robosoft.playverse.feature.presentation.view.profile


import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.playverse.data.models.ProfileTournamentResponse
import com.robosoft.playverse.R
import com.robosoft.playverse.utilities.*
import kotlinx.android.synthetic.main.item_profile_tournaments.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class ProfileTournamentsAdapter(
    private val context: Context,
    private var listener: ProfileClickListener
) :
    RecyclerView.Adapter<ProfileTournamentsAdapter.ViewHolder>() {

    private var timer = Timer()

    private var dataList = mutableListOf<ProfileTournamentResponse>()
    fun setListData(data: List<ProfileTournamentResponse>?) {
        dataList = data as MutableList<ProfileTournamentResponse>
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_profile_tournaments, parent, false)
        return ViewHolder(view)

    }

    fun getMonthByNumber(monthnum:Int):String {
        val c = Calendar.getInstance()
        val month_date = SimpleDateFormat("MMMM")
        c[Calendar.MONTH] = monthnum-1
        return month_date.format(c.time)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tournaments: MutableList<ProfileTournamentResponse> = dataList
        if (position != 0) {
            if (tournaments[position].start_time?.subSequence(
                    0,
                    10
                ) == tournaments[position - 1].start_time?.subSequence(0, 10)
            ) holder.itemView.tvDate.visibility = View.GONE else holder.itemView.tvDate.visibility =
                View.VISIBLE
        }

        holder.itemView.apply {
            tvDate.text = context.getString(R.string.date_format, tournaments[position].start_time?.subSequence(8,10).toString(), getMonthByNumber(tournaments[position].start_time?.subSequence(6,7).toString().toInt()),tournaments[position].start_time?.subSequence(0,4))
            tvName.text = tournaments[position].game_name
            tvUserScore.text = tournaments[position].user_score.toString()
            tvHighScoreNo.text = tournaments[position].current_high_score.toString()
            tvYourRankNo.text = tournaments[position].user_rank.toString()

            if (tournaments[position].user_rank == 1) {
                tvJustPoints.text =
                    context.getString(R.string.congrats_you_have_the_highest_score)
                tvMoreToWin.visibility = View.GONE
            } else {
                tvJustPoints.text = context.getString(
                    R.string.just_points,
                    tournaments[position].target_score.toString()
                )
                tvMoreToWin.text = context.getString(
                    R.string.more_to_win,
                    tournaments[position].target_reward.toString()
                )
            }


            btnContinuePlaying.setOnClickListener {
                listener.getItemData(position = position, tournaments[position])
            }

            ivStats.setOnClickListener {
                listener.onStatsClick(
                    tournaments[position].id.toString(),
                    tournaments[position].toDataItem(),
                    tournaments[position].game_id.toString()
                )
            }
        }

        when (tournaments[position].category) {
            3 -> {
                holder.itemView.apply {
                    imgBg.setImageResource(R.drawable.bg_tournaments_yellow)
                    tvStakes.setBackgroundResource(R.drawable.bg_stakes_yellow)
                    tvStakes.text = "High stake tournament"
                }

            }
            2 -> {
                holder.itemView.apply {
                    imgBg.setImageResource(R.drawable.bg_tournaments_purple)
                    tvStakes.setBackgroundResource(R.drawable.bg_mid_stake)
                    tvStakes.text = "Mid stake tournament"
                }

            }
        }



        if (tournaments[position].status == 5 || tournaments[position].start_time == tournaments[position].end_time) {
            holder.itemView.tvTime.text = "Tournament ended"
            holder.itemView.tvJustPoints.isGone = true
            holder.itemView.tvMoreToWin.isGone = true
            holder.itemView.btnContinuePlaying.isGone = true
            holder.itemView.ivStats.isGone = true
            holder.itemView.btnViewStandings.isVisible = true
            holder.itemView.tvHighScore.text = "Highest score"
            holder.itemView.tvYourRankNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40F);
            holder.itemView.tvTime.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

            holder.itemView.btnViewStandings.setOnClickListener {
                listener.onViewStandingsClick(
                    tournaments[position].id.toString(),
                    tournaments[position].toDataItem()
                )
            }
        } else if (tournaments[position].status != 5) {
            startTimer(
                holder,
                position,
                dataList[position].end_time,
                tournaments
            )
        }
    }

    private fun startTimer(
        holder: ViewHolder,
        position: Int,
        endDate: String,
        tournaments: MutableList<ProfileTournamentResponse>
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

                holder.itemView.tvTime.text =
                    "${d}d: ${h}h: ${m}m: $s" //You can compute the millisUntilFinished on hours/minutes/seconds


                if (d == 0L && s == 0L && m == 0L && h == 0L) {
                    timer.cancel()
                    holder.itemView.tvTime.text = "Tournament ended"
                    holder.itemView.tvJustPoints.isGone = true
                    holder.itemView.tvMoreToWin.isGone = true
                    holder.itemView.btnContinuePlaying.isGone = true
                    holder.itemView.ivStats.isGone = true
                    holder.itemView.btnViewStandings.isVisible = true
                    holder.itemView.tvHighScore.text = "Highest score"
                    holder.itemView.tvYourRankNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40F);
                    holder.itemView.tvTime.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        null,
                        null
                    )

                    holder.itemView.btnViewStandings.setOnClickListener {
                        listener.onViewStandingsClick(
                            tournaments[position].id.toString(),
                            tournaments[position].toDataItem()
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
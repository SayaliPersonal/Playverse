package com.robosoft.playverse.feature.presentation.view.gamedetails

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.playverse.data.models.LeaderboardItem
import com.robosoft.playverse.R
import kotlinx.android.synthetic.main.rank_list.view.*

class RankListAdapter(private val leaderboard: List<LeaderboardItem>, private val userId: String) :
    RecyclerView.Adapter<RankListAdapter.RankViewHolder>() {

    lateinit var context: Context

    class RankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(word: String) {
            itemView.txt_winner_amt.text = word
        }

        var cardView = itemView.constraint_card
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rank_list, parent, false)
        context = parent.context

        return RankViewHolder(view)
    }

    override fun getItemCount(): Int = leaderboard.size

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        holder.itemView.apply {
            txt_rank.text = String.format("%02d", leaderboard[position].rank)
            txt_rank_name.text = leaderboard[position].userName
            txt_winner_amt.text = leaderboard[position].score.toString()
            Glide.with(this).load(leaderboard[position].profileUrl).into(
                img_rank_avatar
            )
        }
        when {
            position % 2 == 1 -> {
                holder.cardView.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.light_blue_border, null)
            }
            leaderboard[position].userId.toString() == userId -> {
                holder.cardView.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.heighlight_row, null)
                holder.itemView.apply {
                    txt_rank.setTextColor(ContextCompat.getColor(context, R.color.white))
                    txt_rank_name.setTextColor(ContextCompat.getColor(context, R.color.white))
                    txt_winner_amt.setTextColor(ContextCompat.getColor(context, R.color.white))
                    txt_rank.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                    txt_rank_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                    txt_winner_amt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22F)
                }
            }
            else -> {
                holder.cardView.background =
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.button_light_blue_border,
                        null
                    )

            }
        }
    }
}
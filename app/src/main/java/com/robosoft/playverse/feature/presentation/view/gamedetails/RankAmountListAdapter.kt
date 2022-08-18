package com.robosoft.playverse.feature.presentation.view.gamedetails

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.playverse.data.models.RewardsItem
import com.robosoft.playverse.R
import kotlinx.android.synthetic.main.rank_list.view.*
import kotlinx.android.synthetic.main.rewards_rank_card.view.*

class RankAmountListAdapter(private val rewards: List<RewardsItem>) :
    RecyclerView.Adapter<RankAmountListAdapter.RankAmountViewHolder>() {

    lateinit var context: Context

    class RankAmountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankAmountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rewards_rank_card, parent, false)
        context = parent.context

        return RankAmountViewHolder(view)
    }

    override fun getItemCount(): Int = rewards.size

    override fun onBindViewHolder(holder: RankAmountViewHolder, position: Int) {
        holder.itemView.apply {
            if (rewards[position].fromRank == rewards[position].toRank) {
                your_score_title.text =
                    "Rank ${rewards[position].fromRank}"
            } else {
                your_score_title.text =
                    "Rank ${rewards[position].fromRank} - ${rewards[position].toRank}"
            }

            txt_rank_amt.text = rewards[position].rewards.toString().split(".0")[0]

        }
    }
}
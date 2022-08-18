package com.robosoft.playverse.feature.presentation.view.gamedetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playverse.data.models.FreeGamePlaysItem
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import kotlinx.android.synthetic.main.transparent_card_view.view.*
import javax.inject.Inject

/**
 * Created by Subham on 7/13/2022.
 */
class GameFreePlayAdapter(
    private val freeGamePlays: List<FreeGamePlaysItem>,
    private val freeGameClickListener: FreeGameClickListener
) :
    RecyclerView.Adapter<GameFreePlayAdapter.FreeGamePlayViewHolder>() {
    @Inject
    lateinit var appStorage: AppStorage
    lateinit var context: Context

    class FreeGamePlayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreeGamePlayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.free_game_play, parent, false)
        context = parent.context

        return FreeGamePlayViewHolder(view)
    }

    override fun getItemCount(): Int = freeGamePlays.size

    override fun onBindViewHolder(holder: FreeGamePlayViewHolder, position: Int) {
        holder.itemView.apply {
            txt_card_des.text =
                "${context.getString(R.string.get_make)} ${freeGamePlays[position].score} \n${
                    context.getString(
                        R.string.and_get
                    )
                } ₹${freeGamePlays[position].reward} ${context.getString(R.string.reward_)}"
            gameFreePlay.setOnClickListener {
                freeGameClickListener.getFreeGamePosition(
                    position,
                    freeGamePlays[position].id.toString()
                )

            }
        }
    }
}

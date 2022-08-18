package com.robosoft.playverse.feature.presentation.view.gamedetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.playverse.data.models.GamesItem
import com.robosoft.playverse.R
import com.robosoft.playverse.feature.presentation.view.funds.GameListItemClickListener
import com.robosoft.playverse.utilities.convertCurrency
import kotlinx.android.synthetic.main.list_card_view.view.*
import kotlinx.android.synthetic.main.list_card_view.view.myCardView

class GameListAdapter(
    private val gamesItem: List<GamesItem>,
    private val gameListItemClickListener: GameListItemClickListener
) :
    RecyclerView.Adapter<GameListAdapter.GameListViewHolder>() {

    lateinit var context: Context

    class GameListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_card_view, parent, false)
        context = parent.context

        return GameListViewHolder(view)
    }

    override fun getItemCount(): Int = gamesItem.size


    override fun onBindViewHolder(holder: GameListViewHolder, position: Int) {
        holder.itemView.apply {
            txt_game_name.text = gamesItem[position].name
            txt_developed_by.text = "by ${gamesItem[position].developer}"
            txt_summery.text =
                "${convertCurrency(gamesItem[position].usersPlayed)} ${context.getString(R.string.playing)}"
            txt__processing_fee_val.text =
                "${convertCurrency(gamesItem[position].gamePlays)} ${context.getString(R.string.game_plays)}"

            myCardView.setOnClickListener {
                gameListItemClickListener.getGameId(position, gamesItem[position].id)
            }

            try {
                Glide.with(this)
                    .load(gamesItem[position].banners[0])
                    .into(
                        constraint_image
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
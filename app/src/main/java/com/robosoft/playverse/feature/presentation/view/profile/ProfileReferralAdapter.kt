package com.robosoft.playverse.feature.presentation.view.profile

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robosoft.playverse.R
import kotlinx.android.synthetic.main.tournamant.view.*
import kotlinx.android.synthetic.main.tournamant_game_info.view.*

class ProfileReferralAdapter: RecyclerView.Adapter<ProfileReferralAdapter.ReferralViewHolder>() {

    class ReferralViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferralViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.referral_count, parent, false)

        return ReferralViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReferralViewHolder, position: Int) {
        val paint = holder.itemView.tvWinAmt.paint
        val width = paint.measureText(holder.itemView.tvWinAmt.text.toString())
        val textShader: Shader = LinearGradient(
            0f, 0f, width, holder.itemView.tvWinAmt.textSize, intArrayOf(
                Color.parseColor("#FFBA00"),
                Color.parseColor("#FFCA3A"),
                Color.parseColor("#FFE597"),
                Color.parseColor("#FFCA3A"),
                Color.parseColor("#D96201")
            ), null, Shader.TileMode.REPEAT
        )

        holder.itemView.tvWinAmt.paint.shader = textShader
    }

    override fun getItemCount(): Int = 5
}
package com.robosoft.playverse.feature.presentation.view.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robosoft.playverse.R

class ProfileTournamentsSectionAdapter(val list: ArrayList<HashMap<String, String?>>): RecyclerView.Adapter<ProfileTournamentsSectionAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = list[position]
        holder.gameName?.text = location.get("name")
        holder.tvStakes.text = location.get("tvStakes")
        if(holder.tvStakes.text == "High stake tournament"){
            holder.imgBg.setImageResource(R.drawable.bg_tournaments_yellow)
            holder.tvStakes.setBackgroundResource(R.drawable.bg_stakes_yellow)
        }else if(holder.tvStakes.text == "Mid stake tournament"){
            holder.imgBg.setImageResource(R.drawable.bg_tournaments_purple)
            holder.tvStakes.setBackgroundResource(R.drawable.bg_mid_stake)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_tournaments_section, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val gameName = itemView.findViewById<TextView>(R.id.tvName)
        val imgBg = itemView.findViewById<ImageView>(R.id.imgBg)
        val tvStakes = itemView.findViewById<TextView>(R.id.tvStakes)
    }

}
package com.robosoft.playverse.feature.presentation.view.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robosoft.playverse.R

class ProfileTournamentsAdapterNew(val time: ArrayList<HashMap<String, String?>>): RecyclerView.Adapter<ProfileTournamentsAdapterNew.ViewHolder>() {

    val byDates = time.groupBy { it["time"] }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Update date label
        /* val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
         val dateList = byDates.values.toMutableList()
         holder.date?.text = sdf.format(dateList[position][0].get("time"))*/
        val dateList = byDates.values.toMutableList()
        holder.date.text = time[position].get("time")?.subSequence(0,10)
        // Create vertical Layout Manager
        holder.rv?.layoutManager = LinearLayoutManager(holder.rv.context, RecyclerView.VERTICAL, false)
        // Access RecyclerView Adapter and load the data
        val adapter = ProfileTournamentsSectionAdapter(dateList[position] as ArrayList<HashMap<String, String?>>)
        holder.rv?.adapter = adapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileTournamentsAdapterNew.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_profile_tournaments, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return byDates.count()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val date = itemView.findViewById<TextView>(R.id.tvDate)
        val rv = itemView.findViewById<RecyclerView>(R.id.rvTournaments)
    }

}
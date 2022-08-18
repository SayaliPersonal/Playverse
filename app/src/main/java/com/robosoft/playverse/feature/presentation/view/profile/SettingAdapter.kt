package com.robosoft.playverse.feature.presentation.view.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robosoft.playverse.R
import kotlinx.android.synthetic.main.setting_item.view.*
import kotlinx.android.synthetic.main.text_cross_button.view.tvGetStarted

class SettingAdapter(
    var list: ArrayList<SettingData>,
    private var getSettingListener: GetSettingListener
) :
    RecyclerView.Adapter<SettingAdapter.MySettingViewHolder>() {

    private lateinit var context: Context

    class MySettingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySettingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.setting_item, parent, false)
        context = parent.context
        return MySettingViewHolder(view)
    }

    override fun onBindViewHolder(holder: MySettingViewHolder, position: Int) {
        holder.itemView.tvGetStarted.text = list[position].title
        holder.itemView.ivImage.setImageDrawable(list[position].image)
        holder.itemView.setting_constraint.setOnClickListener {
            getSettingListener.getSettingItemPosition(
                position = position,
                title = list[position].title
            )
        }
    }

    override fun getItemCount(): Int = list.size
}
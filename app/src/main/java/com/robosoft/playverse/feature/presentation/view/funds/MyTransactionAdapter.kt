package com.robosoft.playverse.feature.presentation.view.funds

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.robosoft.playverse.R

class MyTransactionAdapter(private val mList: List<MyTransModel>) : RecyclerView.Adapter<MyTransactionAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction_history, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val test = mList[position]
        // sets the text to the textview from our itemHolder class
        holder.textView.text = test.money

        if(position ==0) {
            holder.tvMonth.visibility = View.VISIBLE
            holder.div.visibility = View.VISIBLE
        } else {
            holder.tvMonth.visibility = View.GONE
            holder.div.visibility = View.GONE
        }


        val status = test.status
        if(status == 2){
            holder.tvAddCash.text = "Cash withdrawn"
            holder.ivAddCash.setImageResource(R.drawable.ic_withdrawn)
            holder.textView.setTextColor(Color.RED)
        }

        holder.img.setOnClickListener {
            holder.layout.visibility = View.VISIBLE
            holder.img.visibility = View.GONE
            holder.imgUp.visibility = View.VISIBLE
        }

        holder.imgUp.setOnClickListener {
            holder.layout.visibility = View.GONE
            holder.imgUp.visibility = View.GONE
            holder.img.visibility = View.VISIBLE
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.tvAddCashMoney)
        val tvAddCash : TextView = itemView.findViewById(R.id.tvCashAdded)
        val img: ImageView = itemView.findViewById(R.id.ivDownArrow)
        val imgUp: ImageView = itemView.findViewById(R.id.ivUpArrow)
        val layout : ConstraintLayout = itemView.findViewById(R.id.layoutDetails)
        val ivAddCash : ImageView = itemView.findViewById(R.id.ivAddCash)
        val tvMonth : TextView = itemView.findViewById(R.id.tvMonth)
        val div : View = itemView.findViewById(R.id.divider)
    }
}
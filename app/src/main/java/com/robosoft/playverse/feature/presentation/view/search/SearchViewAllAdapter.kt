package com.robosoft.playverse.feature.presentation.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playverse.data.models.SearchMainResponse
import com.robosoft.playverse.databinding.ItemSearchBinding
import com.robosoft.playverse.databinding.ItemSearchViewAllBinding

class SearchViewAllAdapter(
    private val searchList: ArrayList<SearchMainResponse>
) :
    RecyclerView.Adapter<SearchViewAllAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemSearchViewAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(searchList[position])
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    class ViewHolder(var itemBinding: ItemSearchViewAllBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(searchMainResponse: SearchMainResponse) {
            itemBinding.imgMain.setImageResource(searchMainResponse.image ?: 0)
            itemBinding.tvGameName.text = searchMainResponse.name
        }
    }
}

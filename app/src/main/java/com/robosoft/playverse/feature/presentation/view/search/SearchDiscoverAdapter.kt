package com.robosoft.playverse.feature.presentation.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playverse.data.models.SearchMainResponse
import com.robosoft.playverse.databinding.ItemDiscoverBinding
import com.robosoft.playverse.databinding.ItemSearchBinding

class SearchDiscoverAdapter(
    private val searchList: ArrayList<SearchMainResponse>
) :
    RecyclerView.Adapter<SearchDiscoverAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemDiscoverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(searchList[position])
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    class ViewHolder(var itemBinding: ItemDiscoverBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(searchMainResponse: SearchMainResponse) {
            itemBinding.titleDiscover.text = searchMainResponse.name
        }
    }
}

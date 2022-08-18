package com.robosoft.playverse.feature.presentation.view.gamedetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.ItemGameBannerBinding


class GameDetailsViewPagerAdapter(
    private var imageList: List<String>
) : PagerAdapter() {

    override fun getCount(): Int = imageList.size ?: 0
    private lateinit var context: Context

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binder: ItemGameBannerBinding = DataBindingUtil.inflate(
            container.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.item_game_banner, container, false
        )
        context = container.context

        container.addView(binder.root)
        bindGameData(binder, position)
        return binder.root
    }

    private fun bindGameData(gameBinder: ItemGameBannerBinding, imgPosition: Int) {
        Glide.with(context).load(imageList[imgPosition]).into(gameBinder.ivBannerGame)

        gameBinder.notifyChange()

    }

    override fun isViewFromObject(
        view: View,
        objectView: Any
    ): Boolean {
        return view === objectView
    }

    override fun destroyItem(container: ViewGroup, position: Int, objectItem: Any) {
        container.removeView(objectItem as ConstraintLayout?)
    }
}

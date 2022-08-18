package com.robosoft.playverse.feature.presentation.view.getStarted

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.ItemGetStartedBinding


class GetStartedAdapter(
    var imageList: List<Int>,
    var mContext: GetStartedFragment,
) : PagerAdapter() {

    override fun getCount(): Int = imageList.size ?: 0

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binder:ItemGetStartedBinding  = DataBindingUtil.inflate(
            container.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.item_get_started, container, false
        )

        container.addView(binder.root)
        bindGameData(binder, position)
        return binder.root
    }

    private fun bindGameData(startedBanner: ItemGetStartedBinding, imgPosition: Int) {
        startedBanner.imageView.setImageResource(imageList[imgPosition])
        startedBanner.notifyChange()

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
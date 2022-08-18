package com.robosoft.playverse.feature.presentation.view.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.media.AudioManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.robosoft.playverse.MyApp
import com.robosoft.playverse.PlayVerseApp
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FreeplayVideoRowBinding
import com.robosoft.playverse.feature.domain.model.ExoPlayerItem
import com.robosoft.playverse.feature.presentation.view.gamedetails.GameClickListener
import kotlinx.android.synthetic.main.freeplay_video_row.view.*
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class FreePlayAdapterNew(
    var gameClickListener: GameClickListener,
    var freeGamePlayClickListener: FreeGamePlayClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = ArrayList<FreePlayCell>()
    lateinit var context: Context
    private var viewHolderMap: WeakHashMap<Int, FreeViewHolder> = WeakHashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            FreeplayVideoRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return FreeViewHolder(binding)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FreeViewHolder).onBind(FreePlayCellViewModel(items[position]), position)
        viewHolderMap[position] = holder
    }


    override fun getItemCount(): Int = items.size

    fun getSize(): Int = items.size

    fun getViewHolderForPosition(position: Int) = viewHolderMap[position]

    fun setAdapterData(itemss: ArrayList<FreePlayCell>) {
        this.items.addAll(itemss)
        notifyDataSetChanged()
    }

    private fun sendIntent() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "http://www.Playverse.com/homefragment")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }


    inner class FreeViewHolder(val binding: FreeplayVideoRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(viewModel: FreePlayCellViewModel, position: Int) {
            binding.viewmodel = viewModel
            val number = DecimalFormat("###,###,##0")
            val formattedText = number.format(viewModel.data.rewards)
            binding.tvWinfreeplay.text = formattedText
            val paint = binding.tvWinfreeplay.paint
            val width = paint.measureText(binding.tvWinfreeplay.text.toString())
            val textShader: Shader = LinearGradient(0f, 0f, width, binding.tvWinfreeplay.textSize, intArrayOf(
                Color.parseColor("#FFBA00"),
                Color.parseColor("#FFCA3A"),
                Color.parseColor("#FFCA3A"),
                Color.parseColor("#FFCA3A"),
                Color.parseColor("#D96201")
            ), null, Shader.TileMode.REPEAT)

            binding.tvWinfreeplay.paint.setShader(textShader)
            Glide.with(context).load(viewModel.data.gameIcon).diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).into(binding.ivIconfreeplay)
            binding.tvUsePlay.text =
                "${viewModel.data.userCount.toString()} ${itemView.context.getString(R.string.playing)}"
            binding.txGamePlay.text =
                "${viewModel.data.gamePlays.toString()} ${itemView.context.getString(R.string.game_plays)}"
            binding.executePendingBindings()

            binding.txShare.setOnClickListener {
                sendIntent()
            }
            binding.lltextdesc.setOnClickListener {
                gameClickListener.gameDetails(viewModel.data.gameId.toString())
            }
            binding.txStats.setOnClickListener {
                gameClickListener.gameDetails(viewModel.data.gameId.toString())
            }

            binding.videoView.setOnClickListener {
                freeGamePlayClickListener.getFreePlayData(position, viewModel.FreeGameplay())
            }
            binding.playfree.setOnClickListener {
                freeGamePlayClickListener.getFreePlayData(position, viewModel.FreeGameplay())
            }

        }


    }
}
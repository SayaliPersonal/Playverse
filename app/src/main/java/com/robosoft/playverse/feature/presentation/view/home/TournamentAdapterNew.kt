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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.TournamentVideoRowBinding
import com.robosoft.playverse.feature.domain.model.ExoPlayerItem
import com.robosoft.playverse.feature.presentation.view.gamedetails.GameClickListener
import kotlinx.android.synthetic.main.tournament_video_row.view.*
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom.current
import kotlin.collections.ArrayList

class TournamentAdapterNew(
    var buyInClickListener: BuyInClickListener,
    var gameClickListener: GameClickListener,
    var tournamentGamePlayClickListener: TournamentGamePlayClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = ArrayList<TournamentDataCell>()
    lateinit var context: Context
    private var viewHolderMap: WeakHashMap<Int, TournamentViewHolder> = WeakHashMap()

    fun setAdapterData(itemss: ArrayList<TournamentDataCell>) {
        this.items.addAll(itemss)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            TournamentVideoRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return TournamentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TournamentViewHolder).onBind(TournamentCellViewModel(items[position]), position)
        viewHolderMap[position] = holder
    }


    override fun getItemCount(): Int = items.size

    fun getSize(): Int = items.size

    fun getViewHolderForPosition(position: Int) = viewHolderMap[position]

    inner class TournamentViewHolder(val binding: TournamentVideoRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(viewModel: TournamentCellViewModel, position: Int) {
            binding.viewmodeltour = viewModel
            binding.tvTitle.text = viewModel.data.game
            if (viewModel.data.category == "" || viewModel.data.category == null) {
                binding.llstakedesc.visibility = View.GONE
            } else {
                binding.tvInfotour.text = viewModel.data.category.lowercase().split(' ').joinToString(" ") { it.capitalize() } + " tournament"
            }
            Glide.with(context).load(viewModel.data.gameIcon).diskCacheStrategy(
                    DiskCacheStrategy.ALL).into(binding.ivIcontournament)
            binding.tournamentPlay.text =
                "${context.getString(R.string.play_for_20)} ₹${viewModel.data.buyIn.toString()}"
            binding.tvUsePlay.text = "${viewModel.data.userCount.toString()} ${context.getString(R.string.playing)}"
            binding.txGamePlay.text =
                "${viewModel.data.gamePlays.toString()} ${context.getString(R.string.game_plays)}"

            val number = DecimalFormat("###,###,##0")
            val formattedText = number.format(viewModel.data.rewards)
            binding.tvWinAmt.text = formattedText
            val paint = binding.tvWinAmt.paint
            val width = paint.measureText(binding.tvWinAmt.text.toString())
            val textShader: Shader = LinearGradient(0f, 0f, width, binding.tvWinAmt.textSize, intArrayOf(
                Color.parseColor("#FFBA00"),
                Color.parseColor("#FFCA3A"),
                Color.parseColor("#FFCA3A"),
                Color.parseColor("#FFCA3A"),
                Color.parseColor("#D96201")
            ), null, Shader.TileMode.REPEAT)

            binding.tvWinAmt.paint.setShader(textShader)

            binding.txShare.setOnClickListener {
                sendIntent()
            }

            binding.lltextdesc.setOnClickListener {
                gameClickListener.gameDetails(viewModel.data.gameId.toString())
            }
            binding.txStats.setOnClickListener {
                gameClickListener.gameDetails(viewModel.data.gameId.toString())
            }
            binding.tournamentPlay.setOnClickListener {
                buyInClickListener.buyInPopUp(
                    viewModel.data.gameId,
                    viewModel.data.buyIn,
                    viewModel.data.id,
                    gameClickListener,
                    viewModel.data.gameLink)
                tournamentGamePlayClickListener.getTournamentPlayData(
                    position = position,
                    viewModel.TournamentDataList()
                )
            }
            binding.videoView.setOnClickListener {
                buyInClickListener.buyInPopUp(
                    viewModel.data.gameId,
                    viewModel.data.buyIn,
                    viewModel.data.id,
                    gameClickListener,
                    viewModel.data.gameLink)
            }

        }
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

    interface OnVideoPreparedListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }
}
package com.robosoft.playverse.feature.presentation.view.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.playverse.data.models.FreeGameplay
import com.playverse.data.util.Constants
import com.robosoft.playverse.PlayVerseApp
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentFreePlayBinding
import com.robosoft.playverse.feature.domain.model.ExoPlayerItem
import com.robosoft.playverse.feature.domain.model.VideoModel
import com.robosoft.playverse.feature.presentation.view.gamedetails.GameClickListener
import com.robosoft.playverse.feature.presentation.viewModel.FreePlayViewModel
import com.robosoft.playverse.utilities.Connectivity
import com.robosoft.playverse.utilities.TemporaryStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_free_play.*
import kotlinx.android.synthetic.main.freeplay_video_row.view.*
import kotlinx.android.synthetic.main.header_footer.*
import kotlinx.android.synthetic.main.header_footer.view.*
import javax.inject.Inject

@AndroidEntryPoint
class FreePlayFragment : Fragment(), GameClickListener, FreeGamePlayClickListener {

    var freePlayvideos = ArrayList<FreeGameplay>()
    //var freePlayAdapter: FreePlayAdapter? = null
    private val mainViewModel: FreePlayViewModel by viewModels()
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()
    private val videoListnew = arrayListOf<String>()
    private val freeAdapter =  FreePlayAdapterNew( this, this)
    private lateinit var binding: FragmentFreePlayBinding


    @Inject
    lateinit var appStorage: AppStorage

    private val pageStart: Int = 0
    private var totalPages: Int = 0
    private var currentPage: Int = pageStart
    private var itemPerPage = 10

    //exo player
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var exoPlayerView: PlayerView
    private lateinit var httpDataSourceFactory: HttpDataSource.Factory
    private lateinit var defaultDataSourceFactory: DefaultDataSourceFactory
    private lateinit var cacheDataSourceFactory: DataSource.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFreePlayBinding.inflate(inflater, container, false)
        bindUI()
        subscribeUI()
        initExoPlayer()
        return binding.root
    }

    private fun initExoPlayer() {

        httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)

        defaultDataSourceFactory = DefaultDataSourceFactory(
            requireContext(), httpDataSourceFactory
        )

        cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(PlayVerseApp.simpleCache!!)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)


        exoPlayer = ExoPlayer.Builder(requireContext())
            .setMediaSourceFactory(DefaultMediaSourceFactory(httpDataSourceFactory)).build()

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                //   context.toast("Please check you internet connection")
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                val binding = freeAdapter.getViewHolderForPosition(binding.freePlaypager.currentItem)?.binding
                updateMuteState(true)
                exoPlayerView.controllerAutoShow = false
                binding?.volume1?.setImageResource(R.drawable.mute_image)
                if (playbackState == Player.STATE_BUFFERING) {
                    binding?.progressBar?.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    binding?.progressBar?.visibility = View.GONE

                    binding?.volume1?.setOnClickListener {
                        if (isMute()) {
                            binding.volume1.setImageResource(R.drawable.ic_sound)
                            updateMuteState(false)
                        } else {
                            binding.volume1.setImageResource(R.drawable.mute_image)
                            updateMuteState(true)
                        }
                    }
                }
            }
        })

        exoPlayerView = PlayerView(requireContext())
        exoPlayerView.player = exoPlayer
    }

    private fun isMute(): Boolean {
        context?.let {
            getAudioManager(it).getStreamVolume(AudioManager.STREAM_MUSIC).let { volume ->
                return volume == 0
            }
        }
        return false
    }

    private fun getAudioManager(context: Context) =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private fun updateMuteState(mute: Boolean) {
        requireContext().let {
            getAudioManager(it).adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                if (mute) -100 else 100,
                0
            )
        }
    }


    private fun bindUI(){
        setAdapter()
        mainViewModel.getFreePlayList(currentPage, itemPerPage)
        binding.freePlaypager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                println(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                println(position)

            }


           /* override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                println(position)
                val  adapterSize = freeAdapter.getSize() ?: 0
                //custom pagination for viewpager
                //calling api when the position of the adapter is almost at the end
                if(adapterSize-1 == position && currentPage < totalPages) {
                    currentPage+=1
                    mainViewModel.getFreePlayList(currentPage, itemPerPage)
                }

                binding.freePlaypager.setCurrentItem(position, true)

                val binding = freeAdapter.getViewHolderForPosition(position)?.binding
                binding?.viewmodel?.data?.videoUrl?.let { url ->
                    exoPlayer.pause()
                    if (exoPlayerView.parent != null) {
                        (exoPlayerView.parent as ViewGroup).removeAllViews()
                    }
                    binding?.videoView?.addView(exoPlayerView)
                    exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    exoPlayerView.controllerAutoShow = false
                    exoPlayerView.setShutterBackgroundColor(Color.WHITE);
                    exoPlayerView.setOnClickListener {
                        binding?.viewmodel?.FreeGameplay()
                            ?.let { it1 -> getFreePlayData(position, it1) }
                    }
                    exoPlayer.seekTo(0,0)
                    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

                    val videoUri = Uri.parse(url)
                    val mediaItem = MediaItem.fromUri(videoUri)
                    val mediaSource =
                        ProgressiveMediaSource.Factory(httpDataSourceFactory).createMediaSource(mediaItem)
                    exoPlayer.setMediaSource(mediaSource, true)
                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true
                    exoPlayer.play()

                }


            }
        })
    }*/
           override fun onPageSelected(position: Int) {
               super.onPageSelected(position)
               println(position)
               val  adapterSize = freeAdapter.getSize() ?: 0
               //custom pagination for viewpager
               //calling api when the position of the adapter is almost at the end
               if(adapterSize-1 == position && currentPage < totalPages) {
                   currentPage+=1
                   mainViewModel.getFreePlayList(currentPage, itemPerPage)
               }

               binding.freePlaypager.setCurrentItem(position, true)

               val binding = freeAdapter.getViewHolderForPosition(position)?.binding
               binding?.viewmodel?.data?.videoUrl?.let { url ->
                   exoPlayer.pause()


                   if (exoPlayerView.parent != null) {
                       (exoPlayerView.parent as ViewGroup).removeAllViews()
                   }
                   binding?.videoView?.addView(exoPlayerView)
                   exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                   exoPlayerView.controllerAutoShow = false
                   exoPlayerView.setShutterBackgroundColor(Color.WHITE);
                   exoPlayerView.setOnClickListener {
                       binding?.viewmodel?.FreeGameplay()
                           ?.let { it1 -> getFreePlayData(position, it1) }
                   }
                   exoPlayer.seekTo(0,0)
                   exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

                   val videoUri = Uri.parse(url)
                   val mediaItem = MediaItem.fromUri(videoUri)
                   // from cache
                   val mediaSource =
                       ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)


                   exoPlayer.setMediaSource(mediaSource, true)
                   exoPlayer.prepare()
                   exoPlayer.playWhenReady = true
                   exoPlayer.play()

               }


           }
        })
    }

    private fun subscribeUI() {
        binding.lifecycleOwner = viewLifecycleOwner
        mainViewModel.res.observe(viewLifecycleOwner) {
            freePlayvideos = it.data.freeGameplay
            currentPage = it.data.currentPage
            val page =
                if (it.data.totalItems % itemPerPage == 0) it.data.totalItems / itemPerPage else ((it.data.totalItems / itemPerPage) + 1)
            totalPages = page

            val data = mainViewModel.createCellsFromList(it)
            freeAdapter.setAdapterData(data)
            Log.d("****totalPagesFree play",totalPages.toString()+" : "+currentPage.toString())
            startPreLoadingService(data)

        }
    }

    private fun startPreLoadingService(tournamentList: ArrayList<FreePlayCell>) {
        var arr= ArrayList<String>()
        tournamentList.forEach {
            arr?.add( it.videoUrl)
        }
        val preloadingServiceIntent = Intent(context, VideoPreLoadingService::class.java)
        preloadingServiceIntent.putStringArrayListExtra(Constants.VIDEO_LIST, arr)
        context?.startService(preloadingServiceIntent)
    }

    private fun setAdapter()
    {
        binding.freePlaypager.apply {
            adapter = freeAdapter
        }
    }



    override fun onPause() {
        super.onPause()

        exoPlayer.pause()
    }

    override fun onResume() {
        super.onResume()

        exoPlayer.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::exoPlayer.isInitialized) {
            exoPlayer.volume = 0f
            exoPlayer.stop()
            exoPlayer.release()
            exoPlayer.clearMediaItems()
        }
    }

    override fun gameLoad(id: String,gameUrl: String, sessionId: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToGameFragment(
            gameid = id, gameurl = gameUrl, sessionId = sessionId
        )
        findNavController().navigate(action)
    }

    override fun gameDetails(id: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToGameDetailsFragment(
            id
        )
        findNavController().navigate(action)
    }

    override fun statsDetails(id: String) {
       // do nothing
    }

    override fun gameLoadfromPopup(
        gameUrl: String,
        entryFee: Int,
        gameId: Int,
        tournamentId: Int
    ) {
//do nothing
    }

    override fun getFreePlayData(position: Int, data: FreeGameplay) {
        mainViewModel.fetchSessionIdForGame(
            context = requireActivity(),
            gameId = data.gameId.toString(),
            userId = appStorage.userId,
            gameModeType = Constants.FREE_PLAY_MODE,
            gameModeId = data.id.toString()
        )
        mainViewModel.sessionId.observe(viewLifecycleOwner) {
            if (it.success) {
                val action = HomeFragmentDirections.actionHomeFragmentToGameFragment(
                    data.gameId.toString(),
                    gameurl = data.gameLink,
                    sessionId = it.data.sessionId
                )
                findNavController().navigate(action)
            }
        }
    }
}
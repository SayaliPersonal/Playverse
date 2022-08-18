package com.robosoft.playverse.feature.presentation.view.home


import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentGameBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GameFragment : Fragment()  {

    private lateinit var binding: FragmentGameBinding
    private val args: GameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        bindUI()
        return binding.root
    }

    private fun bindUI() {
        binding.ivBack.setOnClickListener {
            show()
        }


    }

    fun show() {
        val factory = LayoutInflater.from(context)
        val dialogView = factory.inflate(R.layout.custom_game_exit, null)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnQuit = dialogView.findViewById<Button>(R.id.btnQuit)
        val customDialog = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            .setView(dialogView)
            .show()
        btnCancel.setOnClickListener {
            try {
                customDialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        btnQuit.setOnClickListener {
            try {
                customDialog.dismiss()
                val action = GameFragmentDirections.actionGameFragmentToGameDetailsFragment(args.gameid)
                findNavController().navigate(action)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        customDialog.setCanceledOnTouchOutside(false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(args.gameurl + "?session_id=${args.sessionId}&env=dev")
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.setDomStorageEnabled(true)
        binding.webView.settings.setAppCachePath("/data/data/" + requireContext().packageName + "/cache")
        binding.webView.settings.setAppCacheEnabled(true)
        binding.webView.settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
    }


    override fun onResume() {
        super.onResume()
        if (view == null) {
            return
        }
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                show()
                true
            } else false
        }
    }

}

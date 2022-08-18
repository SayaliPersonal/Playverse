package com.robosoft.playverse.feature.presentation.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.robosoft.playverse.databinding.FragmentSettingWebViewBinding


class SettingWebView : Fragment() {

    private lateinit var binding: FragmentSettingWebViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingWebViewBinding.inflate(inflater, container, false)
        // onPageFinished and override Url loading.
        binding.webView.apply {
            webViewClient = WebViewClient()

            // this will load the url of the website
            loadUrl("https://www.google.com/")

            // this will enable the javascript settings
            settings.javaScriptEnabled = true

            // if you want to enable zoom feature
            settings.setSupportZoom(true)
        }
        return binding.root
    }
}
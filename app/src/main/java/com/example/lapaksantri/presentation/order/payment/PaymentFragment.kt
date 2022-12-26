package com.example.lapaksantri.presentation.order.payment

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val args: PaymentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                url?.let {
                    if (it.contains("settlement") || it.contains("capture")) {
                        findNavController().previousBackStackEntry?.savedStateHandle?.set("key", "success")
                        findNavController().navigateUp()
                    }
                }
            }
        }
        binding.webView.loadUrl(args.midtransUrl)
        val settings = binding.webView.settings
        settings.javaScriptEnabled = true
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
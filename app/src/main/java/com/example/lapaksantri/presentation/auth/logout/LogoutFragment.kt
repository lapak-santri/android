package com.example.lapaksantri.presentation.auth.logout

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.FragmentLogoutBinding
import com.example.lapaksantri.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LogoutFragment : DialogFragment() {
    private lateinit var binding: FragmentLogoutBinding
    private val viewModel: LogoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnNo.setOnClickListener {
            dialog?.dismiss()
        }
        binding.btnYes.setOnClickListener {
            viewModel.logout()
        }

        observeLogoutResult()
    }

    private fun observeLogoutResult() {
        viewModel.logoutResult
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        findNavController().navigate(R.id.action_logoutFragment_to_loginFragment)
                    }
                    else -> {}
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
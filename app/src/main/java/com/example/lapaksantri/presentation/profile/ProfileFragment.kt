package com.example.lapaksantri.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.FragmentProfileBinding
import com.example.lapaksantri.utils.gone
import com.example.lapaksantri.utils.showErrorSnackbar
import com.example.lapaksantri.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.linearLayoutSignOut.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_logoutFragment)
        }

        observeErrorSnackbar()
        observeUser()
    }

    private fun observeErrorSnackbar() {
        viewModel.errorSnackbar
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                showErrorSnackbar(binding.root, it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeUser() {
        viewModel.user
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                if (state.isLoading) {
                    binding.shimmerLayoutImageProfile.startShimmer()
                    binding.shimmerLayoutName.startShimmer()
                    binding.shimmerLayoutEmail.startShimmer()
                } else {
                    binding.shimmerLayoutImageProfile.stopShimmer()
                    binding.shimmerLayoutName.stopShimmer()
                    binding.shimmerLayoutEmail.stopShimmer()
                    binding.shimmerLayoutImageProfile.gone()
                    binding.shimmerLayoutName.gone()
                    binding.shimmerLayoutEmail.gone()
                    binding.ivProfile.visible()
                    state.data?.let { user ->
                        binding.tvName.text =user.name
                        binding.tvEmail.text =user.email
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.example.lapaksantri.presentation.address.select_address

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.FragmentSelectAddressBinding
import com.example.lapaksantri.utils.Resource
import com.example.lapaksantri.utils.createLoadingDialog
import com.example.lapaksantri.utils.showErrorSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SelectAddressFragment : Fragment() {
    private var  _binding: FragmentSelectAddressBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SelectAddressViewModel by viewModels()
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSelectAddressBinding.inflate(inflater, container, false)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_selectAddressFragment_to_cartFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = createLoadingDialog(requireContext(), layoutInflater)

        addressAdapter = AddressAdapter(
            {
                viewModel.setMainAddressId(it)
            },
            {
            }
        )

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_selectAddressFragment_to_cartFragment)
        }

        binding.btnAddAddress.setOnClickListener {
        }

        binding.btnSelectAddress.setOnClickListener {
            viewModel.setMainAddress()
        }

        observeErrorSnackbar()
        observeAddresses()
        observeSetMainAddressResult()
    }

    private fun observeErrorSnackbar() {
        viewModel.errorSnackbar
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                showErrorSnackbar(binding.root, it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeAddresses() {
        binding.rvAddress.layoutManager = LinearLayoutManager(context)
        binding.rvAddress.adapter = addressAdapter
        (binding.rvAddress.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.addresses
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                if (state.isLoading) {
                    binding.shimmerLayoutAddress.startShimmer()
                } else {
                    binding.shimmerLayoutAddress.stopShimmer()
                    binding.shimmerLayoutAddress.visibility = View.GONE
                    binding.rvAddress.visibility = View.VISIBLE
                    addressAdapter.submitList(state.data)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeSetMainAddressResult() {
        viewModel.setMainAddressResult
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { result ->
                when(result) {
                    is Resource.Error -> {
                        loadingDialog.dismiss()
                        showErrorSnackbar(binding.root, result.message.toString())
                    }
                    is Resource.Loading -> {
                        loadingDialog.show()
                    }
                    is Resource.Success -> {
                        loadingDialog.dismiss()
                        findNavController().navigate(R.id.action_selectAddressFragment_to_cartFragment)
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
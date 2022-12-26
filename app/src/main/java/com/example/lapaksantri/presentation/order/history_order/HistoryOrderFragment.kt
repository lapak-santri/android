package com.example.lapaksantri.presentation.order.history_order

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.FragmentHistoryOrderBinding
import com.example.lapaksantri.utils.gone
import com.example.lapaksantri.utils.showErrorSnackbar
import com.example.lapaksantri.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HistoryOrderFragment : Fragment() {
    private var _binding: FragmentHistoryOrderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryOrderViewModel by viewModels()
    private lateinit var historyOrderAdapter: HistoryOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key")?.observe(
            viewLifecycleOwner) { result ->

            if (result == "success") {
                viewModel.getTransactions()
            }
        }
        historyOrderAdapter = HistoryOrderAdapter {
            val action = HistoryOrderFragmentDirections.actionHistoryOrderFragmentToOrderDetailFragment(it)
            findNavController().navigate(action)
        }

        observeErrorSnackbar()
        observeTransactions()
    }

    private fun observeErrorSnackbar() {
        viewModel.errorSnackbar
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                showErrorSnackbar(binding.root, it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeTransactions() {
        binding.rvAllOrder.layoutManager = LinearLayoutManager(context)
        binding.rvAllOrder.adapter = historyOrderAdapter
        (binding.rvAllOrder.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.transactions
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                if (state.isLoading) {
                    binding.rvAllOrder.gone()
                    binding.shimmerLayoutHistory.visible()
                    binding.shimmerLayoutHistory.startShimmer()
                } else {
                    binding.shimmerLayoutHistory.stopShimmer()
                    binding.shimmerLayoutHistory.gone()
                    historyOrderAdapter.submitList(state.data)
                    binding.rvAllOrder.visible()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
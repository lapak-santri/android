package com.example.lapaksantri.presentation.order.add_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.lapaksantri.databinding.FragmentAddOrderBinding
import com.example.lapaksantri.utils.showErrorSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddOrderFragment : Fragment() {
    private var _binding: FragmentAddOrderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddOrderViewModel by viewModels()
    private lateinit var orderAdapter: AddOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddOrderBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderAdapter = AddOrderAdapter(
            {

            },
            {

            }
        )

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        observeErrorSnackbar()
        observeProduct()
    }

    private fun observeErrorSnackbar() {
        viewModel.errorSnackbar
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                showErrorSnackbar(binding.root, it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeProduct() {
        binding.rvProduct.layoutManager = LinearLayoutManager(context)
        binding.rvProduct.adapter = orderAdapter

        viewModel.products
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                if (state.isLoading) {
                    binding.shimmerLayoutProduct.startShimmer()
                } else {
                    binding.shimmerLayoutProduct.stopShimmer()
                    binding.shimmerLayoutProduct.visibility = View.GONE
                    binding.rvProduct.visibility = View.VISIBLE
                    orderAdapter.submitList(state.data)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
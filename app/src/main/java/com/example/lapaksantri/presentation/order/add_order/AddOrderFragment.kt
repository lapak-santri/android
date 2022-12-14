package com.example.lapaksantri.presentation.order.add_order

import android.app.Dialog
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
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.FragmentAddOrderBinding
import com.example.lapaksantri.utils.Resource
import com.example.lapaksantri.utils.createLoadingDialog
import com.example.lapaksantri.utils.showErrorSnackbar
import com.example.lapaksantri.utils.showSuccessSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddOrderFragment : Fragment() {
    private var _binding: FragmentAddOrderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddOrderViewModel by viewModels()
    private lateinit var orderAdapter: AddOrderAdapter
    private lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddOrderBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = createLoadingDialog(requireContext(), layoutInflater)

        orderAdapter = AddOrderAdapter(
            { product, position ->
                viewModel.plusCart(product)
                orderAdapter.notifyItemChanged(position)
            },
            { product, position ->
                viewModel.minCart(product)
                orderAdapter.notifyItemChanged(position)
            }
        )

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnOrder.setOnClickListener {
            viewModel.addCarts()
        }

        observeErrorSnackbar()
        observeProduct()
        observeAddCartsResult()
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
        (binding.rvProduct.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

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

    private fun observeAddCartsResult() {
        viewModel.addCartsResult
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
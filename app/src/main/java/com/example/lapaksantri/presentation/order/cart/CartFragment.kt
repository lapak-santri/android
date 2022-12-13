package com.example.lapaksantri.presentation.order.cart

import android.app.Dialog
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
import com.example.lapaksantri.databinding.FragmentCartBinding
import com.example.lapaksantri.utils.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    lateinit var cartAdapter: CartAdapter
    private lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = createLoadingDialog(requireContext(), layoutInflater)

        binding.tvDelivery.text = formatRupiah(0.0)

        cartAdapter = CartAdapter(
            { cartId, type ->
                viewModel.updateCart(cartId, type)
            },
            {
                viewModel.deleteCart(it)
            }
        )

        val gc = GregorianCalendar()
        val nowInMillis = gc.timeInMillis

        gc.add(Calendar.DATE, 1)
        val tomorrowInMillis = gc.timeInMillis

        binding.tvDate.text = formatDatePicker(tomorrowInMillis)

        val constraintsBuilder = CalendarConstraints.Builder()
            .setStart(tomorrowInMillis)
            .setValidator(DateValidatorPointForward.from(nowInMillis))
            .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih Tanggal Pengiriman")
            .setCalendarConstraints(constraintsBuilder)
            .setSelection(tomorrowInMillis)
            .build()

        binding.linearLayoutDate.setOnClickListener {
            val datePickerFragment = childFragmentManager.findFragmentByTag("DATE PICKER")
            if (datePickerFragment == null) {
                datePicker.show(childFragmentManager, "DATE PICKER")
            }
        }

        datePicker.addOnPositiveButtonClickListener {
            binding.tvDate.text = formatDatePicker(it)
        }

        observeErrorSnackbar()
        observeCart()
        observeAddress()
        observeUpdateResult()
        observeDeleteResult()
        observeTotalPrice()
    }

    private fun observeErrorSnackbar() {
        viewModel.errorSnackbar
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                showErrorSnackbar(binding.root, it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeCart() {
        binding.rvOrder.layoutManager = LinearLayoutManager(context)
        binding.rvOrder.adapter = cartAdapter
        (binding.rvOrder.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.carts
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                if (state.isLoading) {
                    binding.shimmerLayoutCart.startShimmer()
                } else {
                    binding.shimmerLayoutCart.stopShimmer()
                    binding.shimmerLayoutCart.visibility = View.GONE
                    if (state.data != null && state.data.isNotEmpty()) {
                        binding.rvOrder.visibility = View.VISIBLE
                        binding.scrollView.visibility = View.VISIBLE
                        cartAdapter.submitList(state.data)
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeAddress() {
        viewModel.address
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                if (state.isLoading) {
                    binding.shimmerLayoutAddress.startShimmer()
                } else {
                    binding.shimmerLayoutAddress.stopShimmer()
                    binding.shimmerLayoutAddress.visibility = View.GONE
                    val mainAddress = state.data
                    if (mainAddress != null) {
                        binding.groupAddress.visibility = View.VISIBLE
                        binding.tvName.text = mainAddress.recipient
                        binding.tvAddress.text = mainAddress.detailAddress
                        binding.tvType.text = resources.getString(
                            R.string.address_type,
                            mainAddress.area,
                            mainAddress.phone.replace("+62", "0")
                        )

                        binding.btnConfirmationOrder.setOnClickListener {

                        }
                    } else {
                        binding.tvSelectAddress.visibility = View.VISIBLE

                        binding.btnConfirmationOrder.setOnClickListener {
                            showErrorSnackbar(binding.root, "Pilih Alamat Anda Terlebih Dahulu")
                        }
                    }

                    binding.btnSelectAddress.setOnClickListener {
                        findNavController().navigate(R.id.action_cartFragment_to_selectAddressFragment)
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeUpdateResult() {
        viewModel.updateResult
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
                        viewModel.getCart()
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeDeleteResult() {
        viewModel.deleteResult
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
                        viewModel.getCart()
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeTotalPrice() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.totalPrice.collect {
                binding.tvSubTotal.text = formatRupiah(it)
                binding.tvTotal.text = formatRupiah(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
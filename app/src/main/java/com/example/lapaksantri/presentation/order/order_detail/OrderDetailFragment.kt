package com.example.lapaksantri.presentation.order.order_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.FragmentOrderDetailBinding
import com.example.lapaksantri.utils.formatDate
import com.example.lapaksantri.utils.formatRupiah
import com.example.lapaksantri.utils.gone

class OrderDetailFragment : Fragment() {
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!
    private val args: OrderDetailFragmentArgs by navArgs()
    private lateinit var detailOrderAdapter: DetailOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key")?.observe(
            viewLifecycleOwner) { result ->

            if (result == "success") {
                findNavController().previousBackStackEntry?.savedStateHandle?.set("key", "success")
                binding.constraintLayoutStatus.setBackgroundResource(R.drawable.already_paid_order_bg)
                binding.ivStatus.setImageResource(R.drawable.ic_already_paid)
                binding.tvStatus.text = requireContext().getString(R.string.already_paid)
                binding.btnPayNow.gone()
            }
        }

        if (args.transaction.status == "success") {
            binding.constraintLayoutStatus.setBackgroundResource(R.drawable.already_paid_order_bg)
            binding.ivStatus.setImageResource(R.drawable.ic_already_paid)
            binding.tvStatus.text = requireContext().getString(R.string.already_paid)
        }
        if (args.transaction.status == "failed") {
            binding.ivStatus.setImageResource(R.drawable.ic_error)
            binding.tvStatus.text = requireContext().getString(R.string.order_failed)
        }

        detailOrderAdapter = DetailOrderAdapter()
        binding.rvDetail.layoutManager = LinearLayoutManager(context)
        binding.rvDetail.adapter = detailOrderAdapter
        detailOrderAdapter.submitList(args.transaction.carts)

        val orderDate = formatDate(args.transaction.createdAt)
        val sendDay = orderDate.substring(0,2).toInt() + 1
        val sendDate = "$sendDay ${orderDate.substring(3, orderDate.length)}"

        binding.tvDate.text = requireContext().getString(R.string.order_date, orderDate)
        binding.tvId.text = requireContext().getString(R.string.invoice, args.transaction.invoice)
        binding.tvAddress.text = args.transaction.address
        binding.tvCity.text = args.transaction.districtName
        binding.tvSendDate.text = requireContext().getString(R.string.shipping_date, sendDate)
        binding.tvTotalPrice.text = formatRupiah(args.transaction.priceTotal.toDouble())

        if (args.transaction.status == "") {
            binding.btnPayNow.setOnClickListener {
                val action =
                    OrderDetailFragmentDirections.actionOrderDetailFragmentToPaymentFragment(args.transaction.midtransUrl)
                findNavController().navigate(action)
            }
        } else {
            binding.btnPayNow.gone()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
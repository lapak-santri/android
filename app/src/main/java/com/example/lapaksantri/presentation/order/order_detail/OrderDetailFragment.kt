package com.example.lapaksantri.presentation.order.order_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.FragmentOrderDetailBinding
import com.example.lapaksantri.utils.formatDate
import com.example.lapaksantri.utils.formatRupiah

class OrderDetailFragment : Fragment() {
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!
    private val args: OrderDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDate.text = requireContext().getString(R.string.order_date, formatDate(args.transaction.createdAt))
        binding.tvId.text = requireContext().getString(R.string.invoice, args.transaction.invoice)
        binding.tvAddress.text = args.transaction.address
        binding.tvCity.text = args.transaction.districtName
        binding.tvSendDate.text = requireContext().getString(R.string.shipping_date, formatDate(args.transaction.createdAt))
        binding.tvTotalPrice.text = formatRupiah(args.transaction.priceTotal.toDouble())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
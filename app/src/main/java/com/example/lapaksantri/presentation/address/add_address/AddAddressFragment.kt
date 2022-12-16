package com.example.lapaksantri.presentation.address.add_address

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.FragmentAddAddressBinding
import com.example.lapaksantri.utils.Resource
import com.example.lapaksantri.utils.createLoadingDialog
import com.example.lapaksantri.utils.showErrorSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAddressFragment : Fragment() {
    private var _binding: FragmentAddAddressBinding? = null
    private val binding get() = _binding!!
    private val args: AddAddressFragmentArgs by navArgs()
    private val viewModel: AddAddressViewModel by viewModels()
    private lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = createLoadingDialog(requireContext(), layoutInflater)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSaveAddress.setOnClickListener {
            validateAddAddress()
        }

        args.address?.let { address ->
            binding.textInputDistrict.editText?.setText(address.district)
            binding.textInputVillage.editText?.setText(address.village)
            binding.textInputName.editText?.setText(address.recipient)
            binding.textInputPhoneNumber.editText?.setText(address.phone)
            binding.textInputFullAddress.editText?.setText(address.detailAddress)
            binding.textInputType.editText?.setText(address.area)
            binding.switchIsMain.isChecked = address.isMain
            binding.btnDeleteAddress.visibility = View.VISIBLE
            binding.btnDeleteAddress.setOnClickListener {
                viewModel.deleteAddress(address.id)
            }
        }

        observeAddAddressResult()
        observeUpdateAddressResult()
        observeDeleteAddressResult()
        onInputTextChanged()
    }



    private fun observeAddAddressResult() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addAddressResult.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        loadingDialog.dismiss()
                        showErrorSnackbar(binding.root, result.message.toString())
                    }
                    is Resource.Loading -> {
                        loadingDialog.show()
                    }
                    is Resource.Success -> {
                        loadingDialog.dismiss()
                        findNavController().navigate(R.id.action_addAddressFragment_to_selectAddressFragment)
                    }
                }
            }
        }
    }

    private fun observeUpdateAddressResult() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.updateAddressResult.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        loadingDialog.dismiss()
                        showErrorSnackbar(binding.root, result.message.toString())
                    }
                    is Resource.Loading -> {
                        loadingDialog.show()
                    }
                    is Resource.Success -> {
                        loadingDialog.dismiss()
                        findNavController().navigate(R.id.action_addAddressFragment_to_selectAddressFragment)
                    }
                }
            }
        }
    }

    private fun observeDeleteAddressResult() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.deleteAddressResult.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        loadingDialog.dismiss()
                    }
                    is Resource.Loading -> {
                        loadingDialog.show()
                    }
                    is Resource.Success -> {
                        loadingDialog.dismiss()
                        findNavController().navigate(R.id.action_addAddressFragment_to_selectAddressFragment)
                    }
                }
            }
        }
    }


    private fun validateAddAddress() {
        val recipient = binding.textInputName.editText?.text?.trim().toString()
        val detailAddress = binding.textInputFullAddress.editText?.text?.trim().toString()
        val phone = binding.textInputPhoneNumber.editText?.text?.trim().toString()
        val area = binding.textInputType.editText?.text?.trim().toString()
        val district = binding.textInputDistrict.editText?.text?.trim().toString()
        val village = binding.textInputVillage.editText?.text?.trim().toString()
        val isMain = binding.switchIsMain.isChecked
        when {
            district.isEmpty() -> {
                binding.textInputDistrict.error = "Masukan Kecamatan Anda"
            }
            village.isEmpty() -> {
                binding.textInputVillage.error = "Masukan Kelurahan Anda"
            }
            detailAddress.isEmpty() -> {
                binding.textInputFullAddress.error = "Masukan Alamat Anda"
            }
            recipient.isEmpty() -> {
                binding.textInputName.error = "Masukan Nama Anda"
            }
            phone.isEmpty() -> {
                binding.textInputPhoneNumber.error = "Masukan Nomor Telepon Anda"
            }
            area.isEmpty() -> {
                binding.textInputType.error = "Masukan Rumah / Kantor / Lainnya"
            }
            else -> {
                args.address?.let {
                    viewModel.updateAddress(
                        id = it.id,
                        recipient = recipient,
                        phone = phone,
                        village = village,
                        district = district,
                        detailAddress = detailAddress,
                        area = area,
                        isMain = isMain,
                    )

                } ?: viewModel.addAddress(
                    recipient = recipient,
                    phone = phone,
                    village = village,
                    district = district,
                    detailAddress = detailAddress,
                    area = area,
                    isMain = isMain,
                )
            }
        }
    }

    private fun onInputTextChanged() {
        binding.textInputDistrict.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textInputDistrict.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.textInputVillage.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textInputVillage.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.textInputName.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textInputName.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.textInputFullAddress.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textInputFullAddress.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.textInputPhoneNumber.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textInputPhoneNumber.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.textInputType.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textInputType.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
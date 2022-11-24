package com.example.lapaksantri.presentation.auth.register

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
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
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.FragmentLoginBinding
import com.example.lapaksantri.databinding.FragmentRegisterBinding
import com.example.lapaksantri.utils.Resource
import com.example.lapaksantri.utils.createLoadingDialog
import com.example.lapaksantri.utils.showErrorSnackbar
import com.example.lapaksantri.utils.showSuccessSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog = createLoadingDialog(requireContext(), layoutInflater)

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnRegister.setOnClickListener {
            register()
        }

        observeRegisterResult()
        onInputTextChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun register() {
        with(binding) {
            val name = edRegisterName.text.toString()
            val email = edRegisterEmail.text.toString()
            val password = edRegisterPassword.text.toString()

            if (name.isEmpty()) {
                edRegisterName.requestFocus()
                textInputName.error = "Masukan Nama Anda"
            }else if (email.isEmpty()) {
                edRegisterEmail.requestFocus()
                textInputEmail.error = "Masukan Email Anda"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                textInputEmail.error = "Penulisan Email Anda Salah"
                textInputEmail.requestFocus()
            } else if (password.isEmpty()) {
                edRegisterPassword.requestFocus()
                textInputPassword.error = "Masukan Password Anda"
            } else {
                viewModel.register(name, email, password)
            }
        }
    }

    private fun observeRegisterResult() {
        viewModel.registerResult
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
                        showSuccessSnackbar(binding.root, result.data.toString())
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onInputTextChanged() {
        binding.textInputName.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textInputName.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.textInputEmail.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textInputEmail.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.textInputPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textInputPassword.isErrorEnabled = false
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }
}
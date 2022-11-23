package com.example.lapaksantri.presentation.auth.login

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
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
import com.example.lapaksantri.databinding.FragmentLoginBinding
import com.example.lapaksantri.utils.Resource
import com.example.lapaksantri.utils.createLoadingDialog
import com.example.lapaksantri.utils.showErrorSnackbar
import com.example.lapaksantri.utils.showSuccessSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = createLoadingDialog(requireContext(), layoutInflater)

        binding.tvRegister.setOnClickListener {
        }

        binding.btnLogin.setOnClickListener {
            login()
        }

        observeLoginResult()
        onInputTextChanged()
    }

    private fun login() {
        with(binding) {
            val email = edLoginEmail.text.toString()
            val password = edLoginPassword.text.toString()
            if (email.isBlank()) {
                textInputEmail.error = "Masukan Email Anda"
                textInputEmail.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                textInputEmail.error = "Penulisan Email Anda Salah"
                textInputEmail.requestFocus()
            } else if (password.isBlank()) {
                textInputPassword.error = "Masukan Password Anda"
                textInputPassword.requestFocus()
            } else {
                viewModel.login(email, password)
            }
        }
    }

    private fun observeLoginResult() {
        viewModel.loginResult
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
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onInputTextChanged() {
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
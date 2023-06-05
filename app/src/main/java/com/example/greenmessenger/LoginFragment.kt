package com.example.greenmessenger

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.greenmessenger.databinding.FragmentLoginBinding
import com.example.greenmessenger.model.LoginViewModel
import com.example.greenmessenger.utils.areValidCredential
import com.example.greenmessenger.utils.isValidEmail
import com.example.greenmessenger.utils.isValidPass
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.confirmPasswordText.addTextChangedListener(object :
            TextViewBindingAdapter.OnTextChanged,
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            @SuppressLint("RestrictedApi")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.signupButton.isEnabled = s.toString() == binding.textPass.text.toString()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.textPass.addTextChangedListener(object : TextViewBindingAdapter.OnTextChanged,
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            @SuppressLint("RestrictedApi")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.signupButton.isEnabled =
                    s.toString() == binding.confirmPasswordText.text.toString()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.signuptext.setOnClickListener {
            binding.confirmPasswordText.isVisible = true
            binding.signupButton.isVisible = true
            binding.buttonLogin.isInvisible = true
        }
        firebaseAuth = FirebaseAuth.getInstance()
        binding.signupButton.setOnClickListener {
            if (Pair(
                    binding.textEmail.text.toString(),
                    binding.textPass.text.toString()
                ).areValidCredential()
            ) {
                firebaseAuth.createUserWithEmailAndPassword(
                    binding.textEmail.text.toString().trim(),
                    binding.textPass.text.toString().trim()

                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        findNavController().navigate(R.id.action_loginFragment_to_userPersonalFregment)
                    } else
                        Toast.makeText(context, "Empty Fields Are not Allowed", Toast.LENGTH_SHORT)
                            .show()
                }
            } else {
                Toast.makeText(
                    context,
                    if (binding.textPass.text.toString().isValidPass().not())
                        "Password must be between 6 and 20 characters long"
                    else
                        "Incorrect User or Password format",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.buttonLogin.setOnClickListener {
            if (Pair(
                    binding.textEmail.text.toString(),
                    binding.textPass.text.toString()
                ).areValidCredential()
            ) {
                firebaseAuth.signInWithEmailAndPassword(
                    binding.textEmail.text.toString(),
                    binding.textPass.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        findNavController().navigate(R.id.action_loginFragment_to_messageFragment)
                    } else {
                        Snackbar.make(binding.root, "User not found", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            } else
                Toast.makeText(
                    context,
                    "Incorrect User or Password format",
                    Toast.LENGTH_SHORT
                ).show()

        }
        setHasOptionsMenu(true)
    }
}


package com.jos.journeyonsolo.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jos.journeyonsolo.MainActivity
import com.jos.journeyonsolo.R
import com.jos.journeyonsolo.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setupAction(view)
        playAnimation()
    }

    private fun setupAction(view: View) {
        binding.registerButton.setOnClickListener {
            val toRegisterFragment = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            view.findNavController().navigate(toRegisterFragment)
        }

        binding.loginButton.setOnClickListener {
            inputData(view)
        }
    }

    private fun inputData(view: View) {
        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showErrorDialog()
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    showLoading(true)
                    auth.signInWithEmailAndPassword(email, password).await() // Menunggu hasil login
                    if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        val firebaseUser: FirebaseUser? = auth.currentUser
                        showLoading(false)
                        Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_SHORT).show()
                        updateUI(firebaseUser)
                    }
                } catch (e: Exception) {
                    if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        showLoading(false)
                        showErrorInvalidCredentials()
                        Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            // Navigasi ke home dengan menghapus back stack
            val toHomeFragment = LoginFragmentDirections.actionLoginFragmentToNavigationHome()
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true) // Pastikan fragment login dihapus dari back stack
                .build()
            view?.findNavController()?.navigate(toHomeFragment, navOptions)
        }
    }


    private fun showErrorInvalidCredentials() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.failed_login))
            .setMessage(resources.getString(R.string.invalid_credentials))
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
            }
            .create()
            .show()
    }

    private fun showErrorDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.failed_login))
            .setMessage(resources.getString(R.string.dialog_failed_login))
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
            }
            .create()
            .show()
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleLogin, View.ALPHA, 1f).setDuration(300)
        val textViewEmail =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
        val emailInputText =
            ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(300)
        val textViewPassword =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val passwordInputText =
            ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(300)
        val orText = ObjectAnimator.ofFloat(binding.textOr, View.ALPHA, 1f).setDuration(300)
        val divider1 = ObjectAnimator.ofFloat(binding.divider1, View.ALPHA, 1f).setDuration(300)
        val divider2 = ObjectAnimator.ofFloat(binding.divider2, View.ALPHA, 1f).setDuration(300)
        val btnLogin = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(300)
        val btnRegister =
            ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(orText, divider1, divider2)
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                textViewEmail,
                emailInputText,
                textViewPassword,
                passwordInputText,
                btnLogin,
                together,
                btnRegister
            )
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
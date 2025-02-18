package com.jos.journeyonsolo.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
        setupListeners()
        playAnimation()
    }

    private fun setupListeners() {
        binding.registerButton.setOnClickListener { navigateTo(R.id.registerFragment) }
        binding.loginButton.setOnClickListener { validateAndLogin() }
    }

    private fun validateAndLogin() {
        val email = binding.inputEmail.text.toString().trim()
        val password = binding.inputPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            showErrorDialog(getString(R.string.dialog_failed_login))
        } else {
            performLogin(email, password)
        }
    }

    private fun performLogin(email: String, password: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                showLoading(true)
                auth.signInWithEmailAndPassword(email, password).await()

                if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    showLoading(false)
                    Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_SHORT).show()
                    navigateToHome(auth.currentUser)
                }
            } catch (e: Exception) {
                if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    showLoading(false)
                    showErrorDialog(getString(R.string.invalid_credentials))
                    Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToHome(currentUser: FirebaseUser?) {
        currentUser?.let {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build()
            view?.findNavController()?.navigate(R.id.navigation_home, null, navOptions)
        }
    }

    private fun showErrorDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.failed_login))
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok), null)
            .create()
            .show()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val fadeInViews = listOf(
            binding.titleLogin,
            binding.emailTextView,
            binding.inputEmail,
            binding.passwordTextView,
            binding.inputPassword,
            binding.loginButton,
            binding.textOr,
            binding.divider1,
            binding.divider2,
            binding.registerButton
        )

        AnimatorSet().apply {
            playSequentially(fadeInViews.map { createFadeInAnimator(it, 300) })
            start()
        }
    }

    private fun createFadeInAnimator(view: View, duration: Long) =
        ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).setDuration(duration)

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateTo(fragmentId: Int) {
        view?.findNavController()?.navigate(fragmentId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

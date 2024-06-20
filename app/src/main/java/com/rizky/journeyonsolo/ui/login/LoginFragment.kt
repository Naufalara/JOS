package com.rizky.journeyonsolo.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rizky.journeyonsolo.R
import com.rizky.journeyonsolo.data.Result
import com.rizky.journeyonsolo.databinding.FragmentLoginBinding
import com.rizky.journeyonsolo.ui.ViewModelFactory

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

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

        setupAction(view)
        playAnimation()
    }

    private fun setupAction(view: View){
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
            viewModel.loginUser(email, password).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_SHORT).show()
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(resources.getString(R.string.success_login))
                                .setMessage(resources.getString(R.string.dialog_success_login))
                                .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                                    val toHomeFragment = LoginFragmentDirections.actionLoginFragmentToNavigationHome()
                                    val navOptions = NavOptions.Builder()
                                        .setPopUpTo(R.id.mobile_navigation, true)
                                        .build()
                                    view.findNavController().navigate(toHomeFragment, navOptions)
                                }
                                .create()
                                .show()
                        }

                        is Result.Error -> {
                            showLoading(false)
                            showErrorInvalidCredentials()
                            Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showErrorInvalidCredentials(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.failed_login))
            .setMessage(resources.getString(R.string.invalid_credentials))
            .setPositiveButton(resources.getString(R.string.ok)){_, _ ->
            }
            .create()
            .show()
    }

    private fun showErrorDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.failed_login))
            .setMessage(resources.getString(R.string.dialog_failed_login))
            .setPositiveButton(resources.getString(R.string.ok)){_, _ ->
            }
            .create()
            .show()
    }


    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleLogin, View.ALPHA, 1f).setDuration(300)
        val textViewEmail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
        val emailInputText = ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(300)
        val textViewPassword = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val passwordInputText = ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(300)
        val orText = ObjectAnimator.ofFloat(binding.textOr, View.ALPHA, 1f).setDuration(300)
        val divider1 = ObjectAnimator.ofFloat(binding.divider1, View.ALPHA, 1f).setDuration(300)
        val divider2 = ObjectAnimator.ofFloat(binding.divider2, View.ALPHA, 1f).setDuration(300)
        val btnLogin = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(300)
        val btnRegister = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(orText, divider1, divider2)
        }

        AnimatorSet().apply {
            playSequentially(title, textViewEmail, emailInputText, textViewPassword, passwordInputText, btnLogin, together, btnRegister)
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
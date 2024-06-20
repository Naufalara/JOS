package com.rizky.journeyonsolo.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rizky.journeyonsolo.R
import com.rizky.journeyonsolo.data.Result
import com.rizky.journeyonsolo.databinding.FragmentRegisterBinding
import com.rizky.journeyonsolo.ui.ViewModelFactory

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAction()
        playAnimation()
    }

    private fun setUpAction(){
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.registerButton.setOnClickListener {
            inputData()
        }
    }

    private fun inputData(){
        val username = binding.inputName.text.toString().trim()
        val email = binding.inputEmail.text.toString().trim()
        val password = binding.inputPassword.text.toString().trim()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()){
            showErrorDialog()
        } else{
            viewModel.registerUser(username, email, password).observe(viewLifecycleOwner) {result ->
                if (result != null){
                    when (result){
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_SHORT).show()
                            showLoading(false)
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(resources.getString(R.string.success_register))
                                .setMessage(resources.getString(R.string.dialog_success_register,email))
                                .create()
                                .show()

                        }
                        is Result.Error -> {
                            Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                            showErrorDataIsAlreadyTaken()
                            showLoading(false)
                        }

                    }
                }
            }
        }
    }

    private fun showErrorDataIsAlreadyTaken(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.failed_register))
            .setMessage(resources.getString(R.string.email_is_already_taken))
            .setPositiveButton(resources.getString(R.string.ok)){_, _ ->
            }
            .create()
            .show()
    }

    private fun showErrorDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.failed_register))
            .setMessage(resources.getString(R.string.dialog_failed_register))
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

        val title = ObjectAnimator.ofFloat(binding.titleRegister, View.ALPHA, 1f).setDuration(300)
        val textViewName = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(300)
        val nameInputText = ObjectAnimator.ofFloat(binding.inputName, View.ALPHA, 1f).setDuration(300)
        val textViewEmail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
        val emailInputText = ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(300)
        val textViewPassword = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val passwordInputText = ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(300)
        val btnRegister = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(300)


        AnimatorSet().apply {
            playSequentially(title, textViewName, nameInputText, textViewEmail ,emailInputText, textViewPassword,passwordInputText, btnRegister)
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
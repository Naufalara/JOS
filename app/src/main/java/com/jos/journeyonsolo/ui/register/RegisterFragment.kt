package com.jos.journeyonsolo.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.jos.journeyonsolo.R
import com.jos.journeyonsolo.databinding.FragmentRegisterBinding
import com.jos.journeyonsolo.ui.login.LoginFragmentDirections

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

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

    private fun setUpAction() {
        auth = FirebaseAuth.getInstance()
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.registerButton.setOnClickListener {
            inputData()
        }

    }

    private fun inputData() {
        val username = binding.inputName.text.toString().trim()
        val email = binding.inputEmail.text.toString().trim()
        val password = binding.inputPassword.text.toString().trim()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showErrorDialog()
        } else {
//            viewModel.registerUser(username, email, password).observe(viewLifecycleOwner) {result ->
//                if (result != null){
//                    when (result){
//                        is Result.Loading -> {
//                            showLoading(true)
//                        }
//                        is Result.Success -> {
//                            Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_SHORT).show()
//                            showLoading(false)
//                            MaterialAlertDialogBuilder(requireContext())
//                                .setTitle(resources.getString(R.string.success_register))
//                                .setMessage(resources.getString(R.string.dialog_success_register,email))
//                                .create()
//                                .show()
//
//                        }
//                        is Result.Error -> {
//                            Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
//                            showErrorDataIsAlreadyTaken()
//                            showLoading(false)
//                        }
//
//                    }
//                }
//            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Mendapatkan ID pengguna
                        val userId = auth.currentUser?.uid

                        // Menyimpan data pengguna ke Firestore
                        val userData = hashMapOf(
                            "userId" to userId,
                            "username" to username,
                            "email" to email
                        )

                        // Menggunakan userId sebagai ID dokumen
                        userId?.let {
                            db.collection("users").document(it).set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(requireActivity(), "User data saved successfully", Toast.LENGTH_SHORT).show()
                                    val toLoginFragment = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                                    view?.findNavController()?.navigate(toLoginFragment)
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(requireActivity(), "Error saving user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }


                    }else{
                        Toast.makeText(requireContext(),"Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }

    private fun showErrorDataIsAlreadyTaken() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.failed_register))
            .setMessage(resources.getString(R.string.email_is_already_taken))
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
            }
            .create()
            .show()
    }

    private fun showErrorDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.failed_register))
            .setMessage(resources.getString(R.string.dialog_failed_register))
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

        val title = ObjectAnimator.ofFloat(binding.titleRegister, View.ALPHA, 1f).setDuration(300)
        val textViewName =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(300)
        val nameInputText =
            ObjectAnimator.ofFloat(binding.inputName, View.ALPHA, 1f).setDuration(300)
        val textViewEmail =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
        val emailInputText =
            ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(300)
        val textViewPassword =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val passwordInputText =
            ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(300)
        val btnRegister =
            ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(300)


        AnimatorSet().apply {
            playSequentially(
                title,
                textViewName,
                nameInputText,
                textViewEmail,
                emailInputText,
                textViewPassword,
                passwordInputText,
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
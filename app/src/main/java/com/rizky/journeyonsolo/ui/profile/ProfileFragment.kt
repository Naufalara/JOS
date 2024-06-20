package com.rizky.journeyonsolo.ui.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rizky.journeyonsolo.R
import com.rizky.journeyonsolo.databinding.FragmentProfileBinding
import com.rizky.journeyonsolo.ui.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner){user ->
            user?.let {
                binding.nameProfile.text = user.username
                if (!it.isLogin){
                    val intent = Intent(this, OnBoardingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }

        setUpAction()
        setUpTheme()
    }

    private fun setUpAction(){
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.logout))
                .setMessage(resources.getString(R.string.logout_description))
                .setPositiveButton(resources.getString(R.string.yes)){ _, _ ->
                    viewModel.logout()
                    Toast.makeText(this, resources.getString(R.string.berhasil_logout), Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton(resources.getString(R.string.no)){_,_ ->
                }
                .create()
                .show()

        }

        binding.btnChangeLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

    }

    private fun setUpTheme(){
        viewModel.getThemeSetting().observe(viewLifecycleOwner){isDarkModeActive: Boolean ->
            if (isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
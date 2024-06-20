package com.rizky.journeyonsolo.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.rizky.journeyonsolo.databinding.FragmentOnBoardingBinding
import com.rizky.journeyonsolo.ui.ViewModelFactory

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<OnBoardingViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner){user ->
            if (user.isLogin) {
                val toHomeFragment = OnBoardingFragmentDirections.actionOnBoardingFragmentToNavigationHome()
                view.findNavController().navigate(toHomeFragment)
            } else {
                return@observe
            }
        }

        binding.btnGabung.setOnClickListener {
            val toLoginFragment = OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment()
            view.findNavController().navigate(toLoginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
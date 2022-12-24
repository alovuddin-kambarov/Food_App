package com.uzcoder.foodapp.ui

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.View
import com.uzcoder.foodapp.R
import com.uzcoder.foodapp.databinding.FragmentAboutBurgerBinding


class AboutBurgerFragment : Fragment(R.layout.fragment_about_burger) {

    private var _binding:FragmentAboutBurgerBinding? = null
    private val binding:FragmentAboutBurgerBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAboutBurgerBinding.bind(view)

        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
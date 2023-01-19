package com.aladdin.foodapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.aladdin.foodapp.adapters.ViewPagerAdapter
import com.aladdin.foodapp.databinding.FragmentMainBinding
import com.aladdin.foodapp.utils.MyData


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)


        val viewPagerAdapter = ViewPagerAdapter(this)




        binding.vp.isUserInputEnabled = false
        binding.bottomBar.onItemSelected = {

            binding.vp.currentItem = it
            MyData.index = it

        }

        binding.vp.adapter = viewPagerAdapter


        binding.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomBar.itemActiveIndex = position


            }
        })


        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
package com.uzcoder.foodapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.uzcoder.foodapp.R
import com.uzcoder.foodapp.adapters.ViewPagerAdapter2
import com.uzcoder.foodapp.databinding.FragmentHomeBinding
import com.uzcoder.foodapp.databinding.ItemTabBinding
import com.uzcoder.foodapp.utils.MySharedPreference


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var categoryList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        loadCategory()



        binding.viewpager.adapter = ViewPagerAdapter2(this, categoryList)
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, pos ->
            tab.text = categoryList[pos]
        }.attach()
        setTabs()


        binding.seeAllCategory.setOnClickListener {
            findNavController().navigate(
                R.id.allCattigoriesFragment,
                Bundle(),
                setAnimation().build()
            )
        }


        return binding.root
    }

    private fun loadCategory() {
        categoryList = ArrayList()
        categoryList.add("\uD83C\uDF54  Burger")
        categoryList.add("\uD83C\uDF55  Pitsa")
        categoryList.add("\uD83C\uDF2D  Hot-Dog")
    }

    private fun setTabs() {
        for (i in 0 until binding.tabLayout.tabCount) {
            val itemTabBinding: ItemTabBinding =
                ItemTabBinding.inflate(LayoutInflater.from(binding.root.context))
            itemTabBinding.itemTv.text = categoryList[i]
            // MySharedPreference.typeData = i - 1
            binding.tabLayout.getTabAt(i)?.customView = itemTabBinding.root
            if (i == 0) {
                itemTabBinding.itemTv.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )


                itemTabBinding.back.setBackgroundResource(R.drawable.design2)
            } else {
                itemTabBinding.itemTv.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
                itemTabBinding.back.setBackgroundResource(R.drawable.design1)
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab!!.customView
                try {
                    val itemTabBinding = ItemTabBinding.bind(customView!!)
                    itemTabBinding.itemTv.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.white
                        )
                    )
                    itemTabBinding.back.setBackgroundResource(R.drawable.design2)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab!!.customView
                val itemTabBinding = ItemTabBinding.bind(customView!!)
                itemTabBinding.itemTv.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
                itemTabBinding.back.setBackgroundResource(R.drawable.design1)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun setAnimation(): NavOptions.Builder {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.exit_anim)
            .setExitAnim(R.anim.pop_enter_anim)
            .setPopEnterAnim(R.anim.enter_anim)
            .setPopExitAnim(R.anim.pop_exit_anim)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        binding.viewpager.currentItem = MySharedPreference.typeData!!
        MySharedPreference.typeData = binding.viewpager.currentItem
        super.onResume()

        if (MySharedPreference.isCategory!!)
            binding.motion.transitionToEnd()
        else
            binding.motion.transitionToStart()


    }

    override fun onPause() {
        super.onPause()
        MySharedPreference.typeData = binding.viewpager.currentItem

        MySharedPreference.isCategory = false
    }

}
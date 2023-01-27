package com.aladdin.foodapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.aladdin.foodapp.R
import com.aladdin.foodapp.adapters.ViewPagerAdapter2
import com.aladdin.foodapp.databinding.FragmentHomeBinding
import com.aladdin.foodapp.databinding.ItemTabBinding
import com.aladdin.foodapp.utils.MySharedPreference
import com.aladdin.foodapp.viewmodel.ViewModel
import com.aladdin.foodapp.utils.Status


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var dialog: AlertDialog
    private val binding get() = _binding!!
    private lateinit var categoryList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        categoryList = ArrayList()


        setProgress()
        loadCategory()

        binding.search.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }



        binding.seeAllCategory.setOnClickListener {
            findNavController().navigate(
                R.id.allCattigoriesFragment,
                Bundle(),
                setAnimation().build()
            )
        }


        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun loadCategory() {

        var a = 0

        val viewModel = ViewModelProvider(this)[ViewModel::class.java]
        viewModel.getCategory(binding.root.context).observe(viewLifecycleOwner){


            when (it.status) {
                Status.SUCCESS -> {
                    dialog.dismiss()
                    if (a == 0){

                        it.data!!.forEach {
                            categoryList.add(it.name)
                        }


                        try {
                            binding.viewpager.adapter = ViewPagerAdapter2(this, categoryList)
                        } catch (e: Exception) {
                        }
                        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, pos ->
                            tab.text = categoryList[pos]
                        }.attach()
                        setTabs()
                    }

                    a++

                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                    dialog.dismiss()

                    val dialog2 = AlertDialog.Builder(binding.root.context).create()
                    val view = LayoutInflater.from(binding.root.context)
                        .inflate(com.aladdin.foodapp.R.layout.check_out_dialog, null, false)

                    view.findViewById<TextView>(R.id.tv).text =
                        "Nimadur xato ketti :(\nEhtimol, internet bilan bog'liq muammo bor. Iltimos, internetga ulanib, qayta urinib ko'ring!"
                    view.findViewById<LottieAnimationView>(R.id.animationViews)
                        .setAnimation(R.raw.error_cat)
                    dialog2.setView(view)
                    dialog2.setContentView(view)
                    dialog2.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog2.show()



                }

            }


        }
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

    private fun setProgress() {
        dialog = AlertDialog.Builder(binding.root.context).create()
        val view = LayoutInflater.from(binding.root.context)
            .inflate(com.aladdin.foodapp.R.layout.custom_progress, null, false)
        dialog.setView(view)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        if (MySharedPreference.isCategory!!){

            binding.viewpager.currentItem = MySharedPreference.typeData!!
            MySharedPreference.typeData = binding.viewpager.currentItem
        }
        super.onResume()

        if (MySharedPreference.isCategory!!)
            binding.motion.transitionToEnd()
        else
            binding.motion.endState

        MySharedPreference.typeData = binding.viewpager.currentItem

        //MySharedPreference.isCategory = false

    }

    override fun onPause() {
        super.onPause()
        MySharedPreference.typeData = binding.viewpager.currentItem

        MySharedPreference.isCategory = false
    }

}
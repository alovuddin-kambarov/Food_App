package com.uzcoder.foodapp.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.uzcoder.foodapp.R
import com.uzcoder.foodapp.adapters.MyAdapterBasket
import com.uzcoder.foodapp.databinding.FragmentDeliveryBinding
import com.uzcoder.foodapp.room.AppDatabase
import com.yy.mobile.rollingtextview.CharOrder
import com.yy.mobile.rollingtextview.RollingTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DeliveryFragment : Fragment() {


    private var _binding: FragmentDeliveryBinding? = null

    private val binding get() = _binding!!

    var allPrice = 0

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryBinding.inflate(inflater, container, false)



        val adapter = MyAdapterBasket()

        AppDatabase.getInstants(binding.root.context).dao().getAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {

                it.forEach { it2 ->
                    allPrice += it2.price!!.toInt()
                }
                if (allPrice == 0){
                    binding.need.visibility = View.VISIBLE
                    binding.rv.visibility = View.INVISIBLE
                    binding.button.visibility = View.INVISIBLE
                    binding.savat.visibility = View.INVISIBLE
                    binding.rolling.visibility = View.INVISIBLE
                    binding.lottie.visibility = View.INVISIBLE
                }else{

                    binding.need.visibility = View.INVISIBLE
                    binding.rv.visibility = View.VISIBLE
                    binding.button.visibility = View.VISIBLE
                    binding.savat.visibility = View.VISIBLE
                    binding.rolling.visibility = View.VISIBLE
                    binding.lottie.visibility = View.VISIBLE
                }
                binding.rolling.setText("$allPrice so'm")
                allPrice = 0
                adapter.submitList(it)


            }



        binding.rv.adapter = adapter
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun rollingText(text: String, which: Int) {
        val rollingTextView2 = binding.root.findViewById<RollingTextView>(which)
        rollingTextView2.animationDuration = 1000L
        rollingTextView2.addCharOrder(CharOrder.Alphabet)
        rollingTextView2.animationInterpolator = AccelerateInterpolator()
        rollingTextView2.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                //finish
            }
        })
        rollingTextView2.setText(text)
    }
}
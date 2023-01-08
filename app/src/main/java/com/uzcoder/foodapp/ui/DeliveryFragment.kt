package com.uzcoder.foodapp.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase
import com.uzcoder.foodapp.R
import com.uzcoder.foodapp.adapters.MyAdapterBasket
import com.uzcoder.foodapp.databinding.FragmentDeliveryBinding
import com.uzcoder.foodapp.models.Burger
import com.uzcoder.foodapp.room.AppDatabase
import com.uzcoder.foodapp.utils.ArrayToString
import com.uzcoder.foodapp.utils.MySharedPreference
import com.uzcoder.foodapp.viewmodel.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.coder.youtubeapi.retrofit.Status

class DeliveryFragment : Fragment() {


    private var _binding: FragmentDeliveryBinding? = null

    lateinit var viewModel: ViewModel
    private val binding get() = _binding!!

    var allPrice = 0

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        val arrayList = ArrayList<Burger>()

        val adapter = MyAdapterBasket()
        AppDatabase.getInstants(binding.root.context).dao().getAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {

                arrayList.addAll(it)

                it.forEach { it2 ->
                    allPrice += it2.price!!.toInt()
                }


                if (allPrice < 1) {
                    binding.need.visibility = View.VISIBLE
                    binding.rv.visibility = View.INVISIBLE
                    binding.button.visibility = View.INVISIBLE
                    binding.savat.visibility = View.GONE
                    binding.rolling.visibility = View.INVISIBLE
                    binding.lottie.visibility = View.INVISIBLE
                    binding.basket.playAnimation()

                } else {

                    try {
                        binding.need.visibility = View.INVISIBLE
                        binding.rv.visibility = View.VISIBLE
                        binding.button.visibility = View.VISIBLE
                        binding.savat.visibility = View.VISIBLE
                        binding.rolling.visibility = View.VISIBLE
                        binding.lottie.visibility = View.VISIBLE

                        binding.rolling.setText("$allPrice so'm")
                        adapter.submitList(it)
                        allPrice = 0
                    } catch (e: Exception) {
                    }
                }


            }
        binding.button.setOnClickListener {

            orderGivenSuccess(arrayList)

            println("lalala: " + System.nanoTime().toString())


        }

        binding.rv.adapter = adapter

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


/*    private fun rollingText(text: String, which: Int) {
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
    }*/

    @RequiresApi(Build.VERSION_CODES.N)
    private fun orderGivenSuccess(arrayList: ArrayList<Burger>) {

        val foodNameList = ArrayList<String>()

        arrayList.forEach {
            foodNameList.add(it.name.toString())
        }

        var str = ArrayToString().rep(foodNameList)

        println("nameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee: $str ")
        viewModel.getFoods(
            binding.root.context,
            str,
            MySharedPreference.phoneNumber!!,
            5,
            "katta",
            allPrice

        ).observe(viewLifecycleOwner) {

            when (it.status) {
                Status.SUCCESS -> {
                    val dialog2 = AlertDialog.Builder(binding.root.context).create()
                    val view2 = LayoutInflater.from(binding.root.context)
                        .inflate(R.layout.check_out_dialog, null, false)
                    dialog2.setView(view2)
                    dialog2.setContentView(view2)

                    dialog2.setOnDismissListener {
                        binding.basket.playAnimation()
                    }

                    if (it!!.data!!.ok!!){
                        val db = FirebaseDatabase.getInstance().getReference("foods")
                        db.child("sys").setValue(System.currentTimeMillis())
                    }


                        dialog2.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog2.show()
                    Toast.makeText(binding.root.context, "Success", Toast.LENGTH_SHORT).show()

                    Log.d(ContentValues.TAG, "OnCreate: data22222 = = = = = ${it.data}")

                    AppDatabase.getInstants(binding.root.context).dao().deleteAll()
                    foodNameList.clear()

                    str = null

                    allPrice = 0
                }
                Status.LOADING -> {

                    Toast.makeText(binding.root.context, "Loading", Toast.LENGTH_SHORT).show()

                }
                Status.ERROR -> {

                    Toast.makeText(binding.root.context, "Error", Toast.LENGTH_SHORT).show()
                    Log.d(ContentValues.TAG, "OnCreate: error22222 = = = = = ${it.message}")
                    Log.d(ContentValues.TAG, "OnCreate: data22222 = = = = = ${it.message}")
                    println("Error hihi: ${it.message} ")

                }

            }


        }


    }


}

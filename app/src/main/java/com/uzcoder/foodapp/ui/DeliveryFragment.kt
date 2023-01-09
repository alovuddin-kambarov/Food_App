package com.uzcoder.foodapp.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.database.FirebaseDatabase
import com.uzcoder.foodapp.R
import com.uzcoder.foodapp.adapters.MyAdapterBasket
import com.uzcoder.foodapp.databinding.FragmentDeliveryBinding
import com.uzcoder.foodapp.models.Burger
import com.uzcoder.foodapp.room.AppDatabase
import com.uzcoder.foodapp.utils.ArrayToString
import com.uzcoder.foodapp.utils.MySharedPreference
import com.uzcoder.foodapp.viewmodel.ViewModel
import com.vicmikhailau.maskededittext.MaskedEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.coder.youtubeapi.retrofit.Status

class DeliveryFragment : Fragment() {


    private var _binding: FragmentDeliveryBinding? = null

    lateinit var viewModel: ViewModel
    private var str = ""
    private val binding get() = _binding!!


    var allPrice = 0

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryBinding.inflate(inflater, container, false)

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

            if (MySharedPreference.phoneNumber!!.isBlank()) {
                phone()
            } else {
                orderGivenSuccess(arrayList)
            }

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

    @SuppressLint("CutPasteId", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun orderGivenSuccess(arrayList: ArrayList<Burger>) {

        var a = 0

        val foodNameList = ArrayList<String>()

        str = ""
        foodNameList.clear()
        arrayList.forEach {
            foodNameList.add(it.name.toString())
        }


        str = ArrayToString().rep(foodNameList)

        println("nameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee: $str ")

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        viewModel.getFoods(
            binding.root.context,
            str,
            MySharedPreference.phoneNumber!!,
            5,
            "katta",
            allPrice

        ).observe(viewLifecycleOwner) {

            val dialog2 = AlertDialog.Builder(binding.root.context).create()
            val view2 = LayoutInflater.from(binding.root.context).inflate(R.layout.check_out_dialog, null, false)


            dialog2.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog2.setView(view2)
            dialog2.setContentView(view2)

            str = ""
            foodNameList.clear()
            when (it.status) {
                Status.SUCCESS -> {

                    dialog2.cancel()
                    if (it!!.data!!.ok!!) {
                        view2.findViewById<TextView>(R.id.tv).text =
                            "Buyurtma berildi!\nSiz bilan aloqaga chiqamiz"
                        view2.findViewById<LottieAnimationView>(R.id.animationViews)
                            .setAnimation(R.raw.check_animation)
                        str = ""
                        foodNameList.clear()
                        val db = FirebaseDatabase.getInstance().getReference("foods")
                        db.child("sys").setValue(System.currentTimeMillis())


                        dialog2.setOnDismissListener {
                            binding.basket.playAnimation()
                        }

                        if (a == 0) {
                            dialog2.show()
                        }
                        a++
                   //     Toast.makeText(binding.root.context, "Success", Toast.LENGTH_SHORT).show()

                        Log.d(ContentValues.TAG, "OnCreate: data22222 = = = = = ${it.data}")

                        AppDatabase.getInstants(binding.root.context).dao().deleteAll()


                        allPrice = 0
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Something is wrong!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }
                Status.LOADING -> {

                    view2.findViewById<TextView>(R.id.tv).text =
                        "Yuborilmoqda..."
                    view2.findViewById<LottieAnimationView>(R.id.animationViews)
                        .setAnimation(R.raw.progress)
                    view2.findViewById<LottieAnimationView>(R.id.animationViews).loop(true)
                    if (a == 0) {
                        dialog2.show()
                    }
             //       Toast.makeText(binding.root.context, "Loading", Toast.LENGTH_SHORT).show()

                }
                Status.ERROR -> {

                    dialog2.cancel()
                    view2.findViewById<TextView>(R.id.tv).text =
                        "Internet bilan bog'liq muammo bor. Iltimos, internetga ulanib, qayta urinib ko'ring!"
                    view2.findViewById<LottieAnimationView>(R.id.animationViews)
                        .setAnimation(R.raw.error_cat)
                    if (a == 0) {
                        dialog2.show()
                    }
                    a++
             //       Toast.makeText(binding.root.context, "Error", Toast.LENGTH_SHORT).show()
                    Log.d(ContentValues.TAG, "OnCreate: error22222 = = = = = ${it.message}")
                    Log.d(ContentValues.TAG, "OnCreate: data22222 = = = = = ${it.message}")
                    println("Error hihi: ${it.message} ")

                }

            }


        }


    }


    private fun phone() {
        val dialog = AlertDialog.Builder(binding.root.context).create()
        val view =
            LayoutInflater.from(binding.root.context).inflate(R.layout.phone_dialog, null, false)
        dialog.setView(view)
        dialog.setContentView(view)

        view.findViewById<MaskedEditText>(R.id.phone2)
        view.findViewById<View>(R.id.btn_card2).setOnClickListener {


            if (view.findViewById<MaskedEditText>(R.id.phone2).text.toString().length == 19) {

                MySharedPreference.phoneNumber =
                    view.findViewById<MaskedEditText>(R.id.phone2).text.toString()

                dialog.dismiss()
                Toast.makeText(
                    binding.root.context,
                    "Raqam saqlandi!",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()

            } else {
                Toast.makeText(
                    binding.root.context,
                    "Iltimos, raqamni to'g'ri kiriting!",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }

        }

        dialog.dismiss()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }


}

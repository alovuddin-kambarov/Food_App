package com.aladdin.foodapp.ui

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
import com.aladdin.foodapp.R
import com.aladdin.foodapp.adapters.MyAdapterBasket2
import com.aladdin.foodapp.databinding.FragmentDeliveryBinding
import com.aladdin.foodapp.models.FoodHome
import com.aladdin.foodapp.room.AppDatabase
import com.aladdin.foodapp.utils.ArrayToString
import com.aladdin.foodapp.utils.MyData
import com.aladdin.foodapp.utils.MySharedPreference
import com.aladdin.foodapp.utils.Status
import com.aladdin.foodapp.viewmodel.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.vicmikhailau.maskededittext.MaskedEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class DeliveryFragment : Fragment() {


    private var _binding: FragmentDeliveryBinding? = null

    private lateinit var dialogP: AlertDialog
    private lateinit var viewModel: ViewModel
    private var str = ""
    private val binding get() = _binding!!
    private var setPrice = 0
    private lateinit var arrayList1: ArrayList<FoodHome>


    private var names: String = ""
    private var allPrice = 0

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryBinding.inflate(inflater, container, false)

        arrayList1 = ArrayList()


        setProgress()


        binding.basket.setOnClickListener {

            MyData.lll.set(true)
        }



        AppDatabase.getInstants(binding.root.context).dao().getAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { it ->


                val adapter = MyAdapterBasket2(it)
                Log.d("lalala", ArrayToString().rep(it).toString())
                arrayList1.addAll(it)

                /*         arrayList1 = arrayList1.stream()
                             .collect(
                                 collectingAndThen(toCollection(Supplier {
                                     TreeSet(
                                         comparingInt(arrayList1::getId)
                                     )
                                 }),
                                     Function<R, RR> { ArrayList() })
                             )*/


                //    it.distinctBy { it.id }

                it.forEach { it2 ->
                    allPrice += it2.price.toInt() * it2.count
                    val g = it2.name + "\n"
                    names += g
                }

                Log.d("sasasa", it.toString())

                if (allPrice < 1) {
                    try {
                        binding.need.visibility = View.VISIBLE
                        binding.rv.visibility = View.INVISIBLE
                        binding.button.visibility = View.INVISIBLE
                        binding.savat.visibility = View.GONE
                        binding.rolling.visibility = View.INVISIBLE
                        binding.lottie.visibility = View.INVISIBLE
                        binding.basket.playAnimation()

                    } catch (e: Exception) {
                    }
                } else {

                    try {
                        binding.need.visibility = View.INVISIBLE
                        binding.rv.visibility = View.VISIBLE
                        binding.button.visibility = View.VISIBLE
                        binding.savat.visibility = View.VISIBLE
                        binding.rolling.visibility = View.VISIBLE
                        binding.lottie.visibility = View.VISIBLE

                        binding.rolling.setText("$allPrice so'm")
                        setPrice = allPrice

                        binding.rv.adapter = adapter
                        allPrice = 0
                    } catch (e: Exception) {
                    }
                }

            }
        binding.button.setOnClickListener {

            if (MySharedPreference.phoneNumber!!.isBlank()) {
                phone()
            } else {
                orderGivenSuccess(arrayList1)
            }

        }


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
    private fun orderGivenSuccess(arrayList: ArrayList<FoodHome>) {

        var a = 0

/*        foodNameList = ArrayList<String>()

        str = ""
        foodNameList.clear()

        for (index in arrayList1.indices) {

            foodNameList.add("${arrayList1[index].name} -- ${arrayList1[index].count} ta")



            Log.d("kakaka", arrayList.toString())
        }*/

        /*
         arrayList.forEach {
             if (it.id != it.id){
                 foodNameList.add("${it.name} -- ${it.count} ta")
                 Log.d("kakaka", it.toString())
             }
         }

 */


        //  str = ArrayToString().rep(foodNameList)

//        str = foodNameList.stream()
//            .map<String>(Function { n: String -> n })
//            .collect(Collectors.joining("||", "", ""))


        var phoneNumber = MySharedPreference.phoneNumber!!
        phoneNumber = phoneNumber.replace("(", "", true)
        phoneNumber = phoneNumber.replace(")", "", true)
        phoneNumber = phoneNumber.replace(" ", "", true)
        phoneNumber = phoneNumber.replace("-", "", true)
        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        viewModel.getFoods(
            binding.root.context,
            names,
            phoneNumber,
            5,
            "katta",
            setPrice

        ).observe(viewLifecycleOwner) {

            val dialog2 = AlertDialog.Builder(binding.root.context).create()
            val view2 = LayoutInflater.from(binding.root.context)
                .inflate(R.layout.check_out_dialog, null, false)


            dialog2.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog2.setView(view2)
            dialog2.setContentView(view2)

            val dialog3 = AlertDialog.Builder(binding.root.context).create()
            val view3 = LayoutInflater.from(binding.root.context)
                .inflate(R.layout.check_out_dialog, null, false)


            dialog3.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog3.setView(view3)
            dialog3.setContentView(view3)
//            foodNameList.clear()
            str = ""
            when (it.status) {
                Status.SUCCESS -> {


                    names = ""
                    if (it.data != null) {

                        if (it!!.data!!.ok!!) {
                            str = ""


                            dialog2.setOnShowListener {
                                dialogP.cancel()
                            }
                            dialog2.setOnDismissListener {
                                binding.basket.playAnimation()
                                dialog3.cancel()
                                names = ""
                                arrayList1.clear()
                                AppDatabase.getInstants(binding.root.context).dao().deleteAll()
                                MyData.arrayList.clear()
                            }

                            if (a == 0) {
                                dialogP.cancel()
                                val db = FirebaseDatabase.getInstance().getReference("foods")
                                db.child("sys").setValue(System.currentTimeMillis())

                                view2.findViewById<TextView>(R.id.tv).text =
                                    "Buyurtma berildi!\nSiz bilan aloqaga chiqamiz"
                                view2.findViewById<LottieAnimationView>(R.id.animationViews)
                                    .setAnimation(R.raw.check_animation)
                                dialog2.show()
                                dialog2.setOnShowListener {
                                    dialogP.cancel()
                                    arrayList.clear()
                                }


                                viewModel

                            }
                            a++
                            //     Toast.makeText(binding.root.context, "Success", Toast.LENGTH_SHORT).show()

                            Log.d(ContentValues.TAG, "OnCreate: data22222 = = = = = ${it.data}")

                            AppDatabase.getInstants(binding.root.context).dao().deleteAll()
                            arrayList.clear()

                            allPrice = 0
                        } else {
                            Toast.makeText(
                                binding.root.context,
                                "Something is wrong!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    }


                }
                Status.LOADING -> {


                    dialogP.show()
                    /*       view3.findViewById<TextView>(R.id.tv).text =
                               "Yuborilmoqda..."
                           view3.findViewById<LottieAnimationView>(R.id.animationViews)
                               .setAnimation(R.raw.progress)
                           view3.findViewById<LottieAnimationView>(R.id.animationViews).loop(true)
                           if (a == 0) {
                               dialog3.show()
                           }
       */

                    //       Toast.makeText(binding.root.context, "Loading", Toast.LENGTH_SHORT).show()

                }
                Status.ERROR -> {

                    dialog2.cancel()
                    dialogP.cancel()
                    dialog2.setOnShowListener {
                        dialogP.cancel()
                    }
                    view2.findViewById<TextView>(R.id.tv).text =
                        "Nimadur xato ketti :(\n" +
                                "Ehtimol, internet bilan bog'liq muammo bor. Iltimos, internetga ulanib, qayta urinib ko'ring!"
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


    override fun onPause() {
        super.onPause()

    }

    private fun setProgress() {
        dialogP = AlertDialog.Builder(binding.root.context).create()
        val view = LayoutInflater.from(binding.root.context)
            .inflate(R.layout.custom_progress, null, false)
        dialogP.setView(view)
        dialogP.setContentView(view)
        dialogP.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogP.setCancelable(false)
    }


}

package com.aladdin.foodapp.ui

import android.annotation.SuppressLint
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
import com.aladdin.foodapp.adapters.MyAdapterBasket
import com.aladdin.foodapp.databinding.FragmentDeliveryBinding
import com.aladdin.foodapp.models.FoodBody
import com.aladdin.foodapp.models.FoodHome
import com.aladdin.foodapp.room.AppDatabase
import com.aladdin.foodapp.utils.MyData
import com.aladdin.foodapp.utils.MySharedPreference
import com.aladdin.foodapp.utils.Status
import com.aladdin.foodapp.viewmodel.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.vicmikhailau.maskededittext.MaskedEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody


class DeliveryFragment : Fragment() {


    private var _binding: FragmentDeliveryBinding? = null
    private lateinit var dialogP: AlertDialog
    private lateinit var viewModel: ViewModel
    private val binding get() = _binding!!
    private lateinit var json: JsonElement
    private var setPrice = 0
    private var allPrice = 0
    lateinit var arrayList: ArrayList<FoodHome>

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryBinding.inflate(inflater, container, false)


        setProgress()


        binding.basket.setOnClickListener {
            MyData.lll.set(true)
        }


        getFromRoom()

        binding.button.setOnClickListener {

            if (MySharedPreference.phoneNumber!!.isBlank()) {
                phone()
            } else {
                orderGivenSuccess()
            }

        }


        return binding.root
    }

    @SuppressLint("CheckResult")
    private fun getFromRoom() {

        val adapter = MyAdapterBasket()
        AppDatabase.getInstants(binding.root.context).dao().getAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {

                it.forEach { it2 ->
                    allPrice += it2.price.toInt() * it2.count
                }

                arrayList = ArrayList()
                arrayList.addAll(it)


                json = Gson().toJsonTree(it)

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
                        adapter.submitList(it)
                        allPrice = 0
                    } catch (e: Exception) {
                    }
                }

            }
        binding.rv.adapter = adapter

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("CutPasteId", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun orderGivenSuccess() {

        var a = 0


        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        val foodBody = FoodBody("!:GzxWR(34f", replaceNumber(), arrayList)

        val toJsonTree = Gson().toJsonTree(foodBody)

        Log.d("assaassa ", toJsonTree.toString())

        val create =
            RequestBody.create(MediaType.parse("multipart/form-data"), toJsonTree.toString())

        viewModel.getFoods(
            binding.root.context,
            create
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
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {

                        if (it!!.data!!.ok!!) {

                            dialog2.setOnShowListener {
                                dialogP.cancel()
                            }
                            dialog2.setOnDismissListener {
                                binding.basket.playAnimation()
                                dialog3.cancel()
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
                                }

                                viewModel

                            }
                            a++

                            MyData.aaa.set(true)
                            AppDatabase.getInstants(binding.root.context).dao().deleteAll()

                            allPrice = 0
                        } else {
                            dialogP.cancel()
                            Toast.makeText(
                                binding.root.context,
                                "Nimadur xato :(",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        Log.d("nega ", it.data.toString())

                    }


                }
                Status.LOADING -> {
                    dialogP.show()
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

                    Log.d("nega ", it.message.toString())

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


    private fun replaceNumber(): String {
        var phoneNumber = MySharedPreference.phoneNumber!!
        phoneNumber = phoneNumber.replace("(", "", true)
        phoneNumber = phoneNumber.replace(")", "", true)
        phoneNumber = phoneNumber.replace(" ", "", true)
        phoneNumber = phoneNumber.replace("-", "", true)
        return phoneNumber
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

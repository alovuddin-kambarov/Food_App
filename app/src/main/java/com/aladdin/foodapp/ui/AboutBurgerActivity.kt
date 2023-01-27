package com.aladdin.foodapp.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.aladdin.foodapp.R
import com.aladdin.foodapp.databinding.ActivityAboutBurgerBinding
import com.aladdin.foodapp.models.FoodHome
import com.aladdin.foodapp.room.AppDatabase
import com.aladdin.foodapp.utils.MySharedPreference
import com.aladdin.foodapp.utils.Status
import com.aladdin.foodapp.viewmodel.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.vicmikhailau.maskededittext.MaskedEditText
import com.yy.mobile.rollingtextview.CharOrder.Alphabet
import com.yy.mobile.rollingtextview.RollingTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AboutBurgerActivity : AppCompatActivity() {

    private var burgerPrice = 0

    private lateinit var dialogP: AlertDialog
    lateinit var burger: FoodHome
    var isLiked = false
    lateinit var viewModel: ViewModel
    private var str = ""
    lateinit var binding: ActivityAboutBurgerBinding

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBurgerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        burger = intent.getSerializableExtra("burger") as FoodHome
        burgerPrice = burger.price.toInt()
        Picasso.get().load(burger.image).into(binding.image)
        binding.burgerName.text = burger.name
        binding.burgerPrice.setText(
            burger.price.toString() + " so'm"
        )

        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
          window.statusBarColor = ContextCompat.getColor(this, R.color.asd)

        setProgress()
        closeKeyboard()


        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                //  Picasso.get().load(item.photoUrl).noFade().noPlaceholder().into(ivDetail)
                transition?.removeListener(this)
            }

            override fun onTransitionResume(transition: Transition?) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTransitionPause(transition: Transition?) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTransitionCancel(transition: Transition?) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTransitionStart(transition: Transition?) {
                //To change body of created functions use File | Settings | File Templates.
            }
        })



        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.button.setOnClickListener {
            if (MySharedPreference.phoneNumber!!.isBlank()) {
                phone()
            } else {
                orderGivenSuccess()
            }
        }


        setAnimatedText()
        setLiked()

    }


    private fun phone() {
        val dialog = AlertDialog.Builder(this).create()
        val view = LayoutInflater.from(this).inflate(R.layout.phone_dialog, null, false)
        dialog.setView(view)
        dialog.setContentView(view)

        view.findViewById<MaskedEditText>(R.id.phone2)
        view.findViewById<View>(R.id.btn_card2).setOnClickListener {


            if (view.findViewById<MaskedEditText>(R.id.phone2).text.toString().length == 19) {

                MySharedPreference.phoneNumber =
                    view.findViewById<MaskedEditText>(R.id.phone2).text.toString()

                dialog.dismiss()
                Toast.makeText(
                    this@AboutBurgerActivity,
                    "Raqam saqlandi!",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()

            } else {
                Toast.makeText(
                    this@AboutBurgerActivity,
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

    /*   private fun orderGivenSuccess() {

           val database = FirebaseDatabase.getInstance()
           burger.count = binding.count.getText().toString().toInt()
           val myRef = database.getReference("foods")


           val arrayList = ArrayList<Burger>()

           arrayList.add(burger)

           val toString = myRef.push().key.toString()
           myRef.child(toString).setValue(burger)


           val dialog2 = AlertDialog.Builder(this).create()
           val view2 = LayoutInflater.from(this).inflate(R.layout.check_out_dialog, null, false)
           dialog2.setView(view2)
           dialog2.setContentView(view2)


           binding.count.setText(burger.count.toString())

           dialog2.window?.setBackgroundDrawableResource(android.R.color.transparent)
           dialog2.show()
       }
   */

    @SuppressLint("CutPasteId", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun orderGivenSuccess() {

        var a = 0


        var pos = "kichik"

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, resources
                .getStringArray(com.aladdin.foodapp.R.array.item)
        ) //setting the country_array to spinner

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                pos = p0!!.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

                pos = "kichik"
            }
        }

        str = burger.name.toString() + " -- " + binding.count.getText()
            .toString() + " ta"

        var phoneNumber = MySharedPreference.phoneNumber!!
        phoneNumber = phoneNumber.replace("(", "", true)
        phoneNumber = phoneNumber.replace(")", "", true)
        phoneNumber = phoneNumber.replace(" ", "", true)
        phoneNumber = phoneNumber.replace("-", "", true)
        viewModel.getFoods(
            binding.root.context,
            str,
            phoneNumber,
            5,
            "katta",
            burgerPrice

        ).observe(this) {

            val dialog2 = AlertDialog.Builder(binding.root.context).create()
            val view2 = LayoutInflater.from(binding.root.context)
                .inflate(R.layout.check_out_dialog, null, false)


            dialog2.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog2.setView(view2)
            dialog2.setContentView(view2)

            str = ""
            when (it.status) {
                Status.SUCCESS -> {

                    dialogP.cancel()
                    if (it!!.data!!.ok!!) {
                        view2.findViewById<TextView>(R.id.tv).text =
                            "Buyurtma berildi!\nSiz bilan aloqaga chiqamiz"
                        view2.findViewById<LottieAnimationView>(R.id.animationViews)
                            .setAnimation(R.raw.check_animation)
                        str = ""


                        if (a == 0) {
                            val db = FirebaseDatabase.getInstance().getReference("foods")
                            db.child("sys").setValue(System.currentTimeMillis())
                            dialog2.show()

                            dialog2.setOnShowListener {
                                dialogP.cancel()
                            }
                        }
                        a++


                        Log.d(ContentValues.TAG, "OnCreate: data22222 = = = = = ${it.data}")

                        AppDatabase.getInstants(binding.root.context).dao().deleteAll()

                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Something is wrong!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }
                Status.LOADING -> {

                    dialogP.show()
                    /*        view2.findViewById<TextView>(R.id.tv).text =
                                "Yuborilmoqda..."
                            view2.findViewById<LottieAnimationView>(R.id.animationViews)
                                .setAnimation(R.raw.progress)
                            view2.findViewById<LottieAnimationView>(R.id.animationViews).loop(true)
                            if (a == 0) {
                                dialog2.show()
                            }*/
                    //       Toast.makeText(binding.root.context, "Loading", Toast.LENGTH_SHORT).show()

                }
                Status.ERROR -> {

                    dialog2.setOnShowListener {
                        dialogP.cancel()
                    }
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

    private fun setLiked() {

        binding.like.setOnClickListener {
            if (!isLiked) {
                isLiked = !isLiked
                binding.liked.visibility = View.GONE
                //   binding.likeImage.setImageResource(R.drawable.ic_add_to_basket_red)
                binding.likeImage.playAnimation()
                binding.likeImage.visibility = View.VISIBLE
                binding.likeImage.addValueCallback(
                    KeyPath("Shape Layer", "Rectangle", "Fill"),
                    LottieProperty.COLOR
                ) { if (it.overallProgress < 0.5) Color.GREEN else Color.RED }
                burger.count = binding.count.getText().toString().toInt()
                AppDatabase.getInstants(binding.root.context).dao().add(burger).subscribeOn(
                    Schedulers.io()
                ).observeOn(AndroidSchedulers.mainThread()).subscribe()
            } else {
                isLiked = !isLiked
                binding.likeImage.playAnimation()
                binding.like.visibility = View.VISIBLE
                //    binding.likeImage.setImageResource(R.drawable.ic_add_to_basket)


            }
        }

    }

    private fun setAnimatedText() {
        var a = 1
        val g = burgerPrice

        binding.plus.setOnClickListener {

            a++
            rollingText(a.toString(), R.id.count)

            burgerPrice += g
            rollingText("$burgerPrice so'm", R.id.burger_price)
            rollingText("$burgerPrice so'm", R.id.burger_price)

            // burgerPrice - g

        }
        binding.minus.setOnClickListener {
            if (a > 1) {
                a--
                rollingText(a.toString(), R.id.count)

                burgerPrice -= g
                rollingText("$burgerPrice so'm", R.id.burger_price)
                rollingText("$burgerPrice so'm", R.id.burger_price)

            }

        }

    }

    private fun rollingText(text: String, which: Int) {
        val rollingTextView2 = findViewById<RollingTextView>(which)
        rollingTextView2.animationDuration = 100L
        rollingTextView2.animationInterpolator = AccelerateInterpolator()
        rollingTextView2.addCharOrder(Alphabet)
        rollingTextView2.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                //finish
            }
        })
        rollingTextView2.setText(text)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        MySharedPreference.isCategory = false
        //      MySharedPreference.isCategory = true
    }

    private fun closeKeyboard() {

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)

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
package com.uzcoder.foodapp.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.uzcoder.foodapp.R
import com.uzcoder.foodapp.databinding.ActivityAboutBurgerBinding
import com.uzcoder.foodapp.models.Burger
import com.uzcoder.foodapp.room.AppDatabase
import com.uzcoder.foodapp.utils.MySharedPreference
import com.vicmikhailau.maskededittext.MaskedEditText
import com.yy.mobile.rollingtextview.CharOrder.Alphabet
import com.yy.mobile.rollingtextview.RollingTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AboutBurgerActivity : AppCompatActivity() {

    private var burgerPrice = 0

    var burger = Burger()
    var isLiked = false
    lateinit var binding: ActivityAboutBurgerBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBurgerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        burger = intent.getSerializableExtra("burger") as Burger
        burgerPrice = burger.price!!.toInt()
        Picasso.get().load(burger.image).into(binding.image)
        binding.burgerName.text = burger.name
        binding.burgerPrice.setText(
            burger.price.toString().substring(0, burger.price.toString().length - 3) + " ming"
        )
        burgerPrice =
            burger.price.toString().substring(0, burger.price.toString().length - 3).toInt()

        binding.ball.text = burger.star

        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                //load full-size
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

    private fun orderGivenSuccess() {

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

        binding.plus.setOnClickListener {

            a++
            rollingText(a.toString(), R.id.count)

            burgerPrice++
            if (burgerPrice < 1000) {
                rollingText("$burgerPrice ming", R.id.burger_price)
            } else {
                rollingText("$burgerPrice 000", R.id.burger_price)
            }

        }
        binding.minus.setOnClickListener {
            if (a > 1) {
                a--
                rollingText(a.toString(), R.id.count)

                burgerPrice--
                if (burgerPrice < 1000) {
                    rollingText("$burgerPrice ming", R.id.burger_price)
                } else {
                    rollingText("$burgerPrice 000", R.id.burger_price)
                }
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

        MySharedPreference.isCategory = true
    }

}
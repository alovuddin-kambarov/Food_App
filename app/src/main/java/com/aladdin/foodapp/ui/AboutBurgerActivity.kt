package com.aladdin.foodapp.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
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
import com.aladdin.foodapp.models.FoodBody
import com.aladdin.foodapp.models.FoodHome
import com.aladdin.foodapp.room.AppDatabase
import com.aladdin.foodapp.utils.MySharedPreference
import com.aladdin.foodapp.utils.Status
import com.aladdin.foodapp.viewmodel.ViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.vicmikhailau.maskededittext.MaskedEditText
import com.yy.mobile.rollingtextview.CharOrder.Alphabet
import com.yy.mobile.rollingtextview.RollingTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody


class AboutBurgerActivity : AppCompatActivity() {

    private var burgerPrice = 0

    private lateinit var dialogP: AlertDialog
    lateinit var burger: FoodHome
    private var isLiked = false
    private lateinit var viewModel: ViewModel
    lateinit var binding: ActivityAboutBurgerBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
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
            burger.price + " so'm"
        )

        binding.des.text = burger.description


        setProgress()
        closeKeyboard()

        nothing()



        if (burger.visible == "g"){
            binding.burgerName.paintFlags = binding.burgerName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            binding.burgerName.setTypeface(null, Typeface.BOLD)
        }


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


    @SuppressLint("CutPasteId", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun orderGivenSuccess() {

        var a = 0

        burger.count = binding.count.getText().toString().toInt()
        val arrayList = ArrayList<FoodHome>()
        arrayList.add(burger)

        val foodBody = FoodBody("!:GzxWR(34f", replaceNumber(), arrayList)

        val toString = Gson().toJsonTree(foodBody).toString()
        val create =
            RequestBody.create(MediaType.parse("multipart/form-data"), toString)

        viewModel.getFoods(
            binding.root.context, create
        ).observe(this) {

            val dialog2 = AlertDialog.Builder(binding.root.context).create()
            val view2 = LayoutInflater.from(binding.root.context)
                .inflate(R.layout.check_out_dialog, null, false)


            dialog2.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog2.setView(view2)
            dialog2.setContentView(view2)

            when (it.status) {
                Status.SUCCESS -> {

                    dialogP.cancel()
                    Log.d("asasasasas", it!!.data!!.ok.toString())
                    if (it!!.data!!.ok!!) {
                        view2.findViewById<TextView>(R.id.tv).text =
                            "Buyurtma berildi!\nSiz bilan aloqaga chiqamiz"
                        view2.findViewById<LottieAnimationView>(R.id.animationViews)
                            .setAnimation(R.raw.check_animation)


                        if (a == 0) {
                            /*      val db = FirebaseDatabase.getInstance().getReference("foods")
                                  db.child("sys").setValue(System.currentTimeMillis())*/
                            dialog2.show()

                            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                            val user: MutableMap<String, Any> = HashMap()
                            user["azizbek"] = System.currentTimeMillis().toString()
                            db.collection("azizbek").document("azizbek").update(user)
                            dialog2.setOnShowListener {
                                dialogP.cancel()
                            }

                        }
                        a++


                        AppDatabase.getInstants(binding.root.context).dao().deleteAll()

                    } else {
                        dialogP.cancel()
                        dialog2.cancel()
                        dialog2.setOnShowListener {
                            dialogP.cancel()
                        }
                        view2.findViewById<TextView>(R.id.tv).text =
                            "Bu taom yoki mahsulot qolmagan. U yana qayta tayyorlanganidan keyin, buni buyurtma berolishingiz mumkin!"
                        view2.findViewById<LottieAnimationView>(R.id.animationViews)
                            .setAnimation(R.raw.cooking)
                        view2.findViewById<LottieAnimationView>(R.id.animationViews)
                        if (a == 0) {
                            dialog2.show()
                        }
                        a++


                    }


                }
                Status.LOADING -> {
                    dialogP.show()
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

                }

            }


        }


    }

    private fun setLiked() {

        binding.like.setOnClickListener {
            if (!isLiked) {
                isLiked = !isLiked
                binding.liked.visibility = View.GONE
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

    private fun replaceNumber(): String {
        var phoneNumber = MySharedPreference.phoneNumber!!
        phoneNumber = phoneNumber.replace("(", "", true)
        phoneNumber = phoneNumber.replace(")", "", true)
        phoneNumber = phoneNumber.replace(" ", "", true)
        phoneNumber = phoneNumber.replace("-", "", true)
        return phoneNumber
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun nothing() {


        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.asd)

        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                transition?.removeListener(this)
            }

            override fun onTransitionResume(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
            }

            override fun onTransitionStart(transition: Transition?) {
            }
        })
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


    private fun turnOnGPS() {

        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(binding.root.context)
            .checkLocationSettings(builder.build())



        result.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)


            } catch (e: ApiException) {

                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                        try {

                            val resolvableApiException = e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                this,
                                10001
                            )


                        } catch (e: IntentSender.SendIntentException) {
                            e.printStackTrace()
                        }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                        //Device does not have location
                    }
                }
            }
        }


    }



}
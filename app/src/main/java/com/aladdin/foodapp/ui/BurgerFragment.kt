package com.aladdin.foodapp.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.aladdin.foodapp.R
import com.aladdin.foodapp.adapters.MyAdapter
import com.aladdin.foodapp.databinding.FragmentBurgerBinding
import com.aladdin.foodapp.models.FoodHome
import com.aladdin.foodapp.utils.MyData
import com.aladdin.foodapp.utils.NetworkChecker
import com.aladdin.foodapp.utils.Status
import com.aladdin.foodapp.viewmodel.ViewModel

class BurgerFragment(var pos: String) : Fragment() {

    private lateinit var myAdapter: MyAdapter
    private lateinit var dialog: AlertDialog
    private lateinit var dialog2: AlertDialog
    private var _binding: FragmentBurgerBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: ViewModel

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBurgerBinding.inflate(inflater, container, false)


        try {
            dialog()

            getData()
            binding.swiperefresh.setOnRefreshListener {
                getData()
            }
            setProgress()
        } catch (e: Exception) {
        }





        return binding.root
    }


    private fun getData() {
        NetworkChecker(binding.root.context).observe(viewLifecycleOwner) {

            if (it) {
                dialog2.cancel()
                Handler(Looper.myLooper()!!).postDelayed({
                    try {
                        viewModel = ViewModelProvider(this)[ViewModel::class.java]
                        var a = 0
                        viewModel.getFoodByCate(binding.root.context, pos)
                            .observe(viewLifecycleOwner) {

                                when (it.status) {
                                    Status.SUCCESS -> {

                                        dialog.cancel()
                                        binding.swiperefresh.isRefreshing = false
                                        binding.rv.visibility = View.VISIBLE
                                        if (a == 0) {

                                            val arrayList = it.data!!

                                            try {
                                                myAdapter =
                                                    MyAdapter(
                                                        arrayList,
                                                        object : MyAdapter.OnClick {
                                                            @SuppressLint("NotifyDataSetChanged")
                                                            override fun click(
                                                                pos: Int,
                                                                imageView: ImageView,
                                                                star: ImageView,
                                                                textView: TextView,
                                                                ball: TextView,
                                                                burger: FoodHome,
                                                            ) {


                                                                val detailIntent =
                                                                    Intent(
                                                                        requireActivity(),
                                                                        AboutBurgerActivity::class.java
                                                                    ).putExtra("burger", burger)

                                                                val imageViewPair =
                                                                    androidx.core.util.Pair(
                                                                        imageView as View,
                                                                        "small"
                                                                    )
                                                                /*  val starPair =
                                                                      androidx.core.util.Pair(
                                                                          star as View,
                                                                          "star_icon"
                                                                      )*/
                                                                val ballPair =
                                                                    androidx.core.util.Pair(
                                                                        star as View,
                                                                        "ball"
                                                                    )

                                                                val options =
                                                                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                                                                        requireActivity(),
                                                                        imageViewPair,
                                                                        // starPair,
                                                                        ballPair
                                                                    )
                                                                startActivity(
                                                                    detailIntent,
                                                                    options.toBundle()
                                                                )
                                                            }
                                                        })
                                            } catch (e: Exception) {
                                            }
                                            binding.rv.adapter = myAdapter
                                            val g = MyData.aaa
                                            g.get().observe(viewLifecycleOwner) {

                                                if (it) {
                                                    binding.rv.adapter = myAdapter
                                                }

                                            }
                                        }
                                        a++
                                    }
                                    Status.LOADING -> {}
                                    Status.ERROR -> {
                                        dialog.cancel()
                                        binding.rv.visibility = View.GONE


                                    }
                                }

                            }
                    } catch (e: Exception) {
                    }


                }, 500)
            } else {

                dialog2.show()
            }

        }

    }

    @SuppressLint("SetTextI18n")
    private fun dialog() {
        dialog2 = AlertDialog.Builder(binding.root.context).create()
        val view2 = LayoutInflater.from(binding.root.context)
            .inflate(R.layout.check_out_dialog, null, false)


        dialog2.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog2.setView(view2)
        view2.findViewById<TextView>(R.id.tv).text =
            "Nimadur xato ketti :(\n" +
                    "Ehtimol, internet bilan bog'liq muammo bor. Iltimos, internetga ulanib, qayta urinib ko'ring!"
        view2.findViewById<LottieAnimationView>(R.id.animationViews)
            .setAnimation(R.raw.error_cat)


    }


    private fun setProgress() {
        dialog = AlertDialog.Builder(binding.root.context).create()
        val view = LayoutInflater.from(binding.root.context)
            .inflate(R.layout.custom_progress, null, false)
        dialog.setView(view)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        try {
            dialog.show()
        } catch (e: Exception) {
        }
    }


}
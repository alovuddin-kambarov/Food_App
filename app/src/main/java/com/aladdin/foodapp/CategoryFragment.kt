package com.aladdin.foodapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.aladdin.foodapp.adapters.MyAdapter
import com.aladdin.foodapp.databinding.FragmentCategoryBinding
import com.aladdin.foodapp.models.FoodHome
import com.aladdin.foodapp.ui.AboutBurgerActivity
import com.aladdin.foodapp.utils.MyData
import com.aladdin.foodapp.utils.NetworkChecker
import com.aladdin.foodapp.utils.Status
import com.aladdin.foodapp.viewmodel.ViewModel


class   CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: AlertDialog
    private lateinit var myAdapter: MyAdapter
    private lateinit var dialog2: android.app.AlertDialog
    var name = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)


        try {

            name = arguments?.getString("burgerName").toString()

            binding.tv.text = name

            dialog()
            Handler(Looper.myLooper()!!).postDelayed({

                getData()
            }, 600)

            binding.swiperefresh.setOnRefreshListener {
                binding.rv.visibility = View.GONE
                getData()
            }


            binding.back.setOnClickListener {
                findNavController().popBackStack()
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
                        val viewModel = ViewModelProvider(this)[ViewModel::class.java]
                        var a = 0
                        viewModel.getFoodByCate(binding.root.context, name)
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
        dialog2 = android.app.AlertDialog.Builder(binding.root.context).create()
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
            .inflate(com.aladdin.foodapp.R.layout.custom_progress, null, false)
        dialog.setView(view)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
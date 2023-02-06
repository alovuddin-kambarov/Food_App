package com.aladdin.foodapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.aladdin.foodapp.R
import com.aladdin.foodapp.adapters.MyOrderAdapter
import com.aladdin.foodapp.databinding.FragmentOrdersBinding
import com.aladdin.foodapp.utils.MySharedPreference
import com.aladdin.foodapp.utils.Status
import com.aladdin.foodapp.viewmodel.ViewModel

class  OrdersFragment : Fragment(R.layout.fragment_orders) {

    private var _binding: FragmentOrdersBinding? = null
    private val binding: FragmentOrdersBinding get() = _binding!!
    private lateinit var viewModel: ViewModel
    private lateinit var dialog: AlertDialog

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)

        setProgress()
        Handler(Looper.myLooper()!!).postDelayed({

            loadOrders()
        }, 600)

        binding.swiperefresh.setOnRefreshListener {
            binding.rv.visibility = View.GONE
            loadOrders()
        }


        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }


        if (MySharedPreference.phoneNumber.isNullOrEmpty()){
            binding.rv.visibility = View.GONE
            binding.savat.visibility = View.GONE
            binding.lottie.visibility = View.GONE
            binding.animationViews.visibility = View.VISIBLE
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun loadOrders() {
        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        viewModel.getOrders(binding.root.context, replaceNumber()).observe(viewLifecycleOwner) {

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
                    var allPrice = 0
                    it.data!!.forEach { order ->
                        allPrice += order.price.toInt() * order.count.toInt()

                    }

                    if (it.data.isNotEmpty()){

                        when (it.data[0].processing) {
                            "t" -> {
                            binding.savat.text = "Tayyormanmoqda..."
                            binding.savat.visibility = View.VISIBLE
                            binding.lottie.setAnimation(R.raw.cooking)
                            binding.lottie.visibility = View.VISIBLE
                        }
                            "y" -> {
                            binding.savat.text = "Yetkazilmoqda..."
                            binding.lottie.setAnimation(R.raw.delivering)
                            binding.lottie.visibility = View.VISIBLE
                            binding.savat.visibility = View.VISIBLE
                        }
                            else -> {

                            binding.rv.visibility = View.GONE
                            binding.savat.visibility = View.GONE
                            binding.lottie.visibility = View.GONE
                            binding.animationViews.visibility = View.VISIBLE
                        }
                        }
                    }else{
                        binding.animationViews.visibility = View.VISIBLE
                    }

                    binding.rolling.setText("$allPrice so'm")

                    binding.rv.visibility = View.VISIBLE
                    binding.savat.visibility = View.VISIBLE
                    binding.lottie.visibility = View.VISIBLE
                    dialog.cancel()
                    binding.swiperefresh.isRefreshing = false
                    if (!it.data.isNullOrEmpty()) {
                        binding.rv.adapter = MyOrderAdapter(it.data)
                        binding.animationViews.visibility = View.GONE
                    } else {
                        binding.animationViews.visibility = View.VISIBLE
                        binding.rv.visibility = View.GONE
                        binding.savat.visibility = View.GONE
                        binding.lottie.visibility = View.GONE
                        binding.rolling.visibility = View.GONE
                    }

                    Log.d("nanana ", it.message.toString())

                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                    dialog.cancel()
                    binding.swiperefresh.isRefreshing = false
                    dialog2.cancel()
                    Log.d("nanana ", it.message.toString())
                    view2.findViewById<TextView>(R.id.tv).text =
                        "Nimadur xato ketti :(\n" +
                                "Ehtimol, internet bilan bog'liq muammo bor. Iltimos, internetga ulanib, qayta urinib ko'ring!"
                    view2.findViewById<LottieAnimationView>(R.id.animationViews)
                        .setAnimation(R.raw.error_cat)

                }

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

    private fun setProgress() {
        dialog = AlertDialog.Builder(binding.root.context).create()
        val view = LayoutInflater.from(binding.root.context)
            .inflate(R.layout.custom_progress, null, false)
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
package com.aladdin.foodapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
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


class OrdersFragment : Fragment(R.layout.fragment_orders) {

    private var _binding: FragmentOrdersBinding? = null
    private val binding: FragmentOrdersBinding get() = _binding!!
    lateinit var viewModel: ViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)


        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        var phoneNumber = MySharedPreference.phoneNumber!!
        phoneNumber = phoneNumber.replace("(", "", true)
        phoneNumber = phoneNumber.replace(")", "", true)
        phoneNumber = phoneNumber.replace(" ", "", true)
        phoneNumber = phoneNumber.replace("-", "", true)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        viewModel.getOrders(binding.root.context, phoneNumber).observe(viewLifecycleOwner) {

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

                    if (!it.data.isNullOrEmpty()){
                        binding.rv.adapter = MyOrderAdapter(it.data)
                        binding.animationViews.visibility = View.GONE
                    }else{
                        binding.animationViews.visibility = View.VISIBLE
                    }


                    Log.d("nanana ", it.message.toString())

                }
                Status.LOADING -> {


                }
                Status.ERROR -> {

                    dialog2.cancel()
                    view2.findViewById<TextView>(R.id.tv).text =
                        "Nimadur xato ketti :(\n" +
                                "Ehtimol, internet bilan bog'liq muammo bor. Iltimos, internetga ulanib, qayta urinib ko'ring!"
                    view2.findViewById<LottieAnimationView>(R.id.animationViews)
                        .setAnimation(R.raw.error_cat)

                }

            }


        }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
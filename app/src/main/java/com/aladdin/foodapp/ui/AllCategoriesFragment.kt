package com.aladdin.foodapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aladdin.foodapp.adapters.CategoryAdapter
import com.aladdin.foodapp.databinding.FragmentAllCattigoriesBinding
import com.aladdin.foodapp.utils.MySharedPreference
import com.aladdin.foodapp.utils.Status
import com.aladdin.foodapp.viewmodel.ViewModel

class AllCategoriesFragment : Fragment() {

    private var _binding: FragmentAllCattigoriesBinding? = null

    private lateinit var dialog: AlertDialog
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllCattigoriesBinding.inflate(inflater, container, false)

        Handler(Looper.myLooper()!!).postDelayed({

            loadCategory()
        },600)

        binding.swiperefresh.setOnRefreshListener {
            binding.rv.visibility = View.GONE
            loadCategory()
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        setProgress()
        return binding.root
    }


    private fun loadCategory() {

        val viewModel = ViewModelProvider(this)[ViewModel::class.java]
        viewModel.getCategory(binding.root.context).observe(viewLifecycleOwner) {


            when (it.status) {
                Status.SUCCESS -> {

                    binding.tv.visibility = View.VISIBLE
                    binding.rv.visibility = View.VISIBLE
                    binding.animationViews.visibility = View.GONE
                    binding.tvInternet.visibility = View.GONE
                    dialog.dismiss()

                    binding.swiperefresh.isRefreshing = false
                    binding.rv.adapter =
                        CategoryAdapter(it.data!!, object : CategoryAdapter.OnClick {
                            override fun click(
                                pos: Int,
                                imageView: ImageView,
                                star: ImageView,
                                textView: TextView,
                                ball: TextView
                            ) {
                                findNavController().popBackStack()
                                MySharedPreference.typeData = pos
                                MySharedPreference.isCategory = true
                            }
                        })


                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                    binding.tv.visibility = View.GONE
                    binding.rv.visibility = View.GONE
                    binding.animationViews.visibility = View.VISIBLE
                    binding.tvInternet.visibility = View.VISIBLE
                    dialog.dismiss()

                    binding.swiperefresh.isRefreshing = false

                }

            }


        }
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
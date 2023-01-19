package com.aladdin.foodapp.ui

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aladdin.foodapp.adapters.CategoryAdapter
import com.aladdin.foodapp.databinding.FragmentAllCattigoriesBinding
import com.aladdin.foodapp.utils.MySharedPreference
import com.aladdin.foodapp.viewmodel.ViewModel
import com.aladdin.foodapp.utils.Status

class AllCategoriesFragment : Fragment() {

    private var _binding: FragmentAllCattigoriesBinding? = null

    private lateinit var dialog: AlertDialog
    private val binding get() = _binding!!
    private var categoryList = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllCattigoriesBinding.inflate(inflater, container, false)

        loadCategory()

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        setProgress()
        return binding.root
    }






    private fun loadCategory() {

        val viewModel = ViewModelProvider(this)[ViewModel::class.java]
        viewModel.getCategory(binding.root.context).observe(viewLifecycleOwner){


            when (it.status) {
                Status.SUCCESS -> {

                    binding.tv.visibility = View.VISIBLE
                    binding.rv.visibility = View.VISIBLE
                    binding.animationViews.visibility = View.GONE
                    binding.tvInternet.visibility = View.GONE
                    dialog.dismiss()

                    binding.rv.adapter = CategoryAdapter(it.data!!, object : CategoryAdapter.OnClick {
                        override fun click(
                            pos: Int,
                            imageView: ImageView,
                            star: ImageView,
                            textView: TextView,
                            ball: TextView
                        ) {
                            /* val fragmentNavigatorExtras = FragmentNavigatorExtras(imageView to "big")
                             findNavController().navigate(R.id.aboutBurgerFragment,null,null,fragmentNavigatorExtras)*/

                            /*  val detailIntent = Intent(requireActivity(), AboutBurgerActivity::class.java)
                              val imageViewPair = androidx.core.util.Pair(imageView as View, "small")

                              val textViewPair =
                                  androidx.core.util.Pair.create<View, String>(textView as View, "burger_name")
                              val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                  requireActivity(),
                                  imageViewPair
                              )
                              //detailIntent.putExtra(TestActivity.DATA, item)
                              startActivity(detailIntent, options.toBundle())*/
                            findNavController().popBackStack()
                            MySharedPreference.typeData = pos
                            MySharedPreference.isCategory = true
                        }
                    })


                }
                Status.LOADING -> {

                    dialog.show()
                }
                Status.ERROR -> {

                    binding.tv.visibility = View.GONE
                    binding.rv.visibility = View.GONE
                    binding.animationViews.visibility = View.VISIBLE
                    binding.tvInternet.visibility = View.VISIBLE
                    dialog.dismiss()


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
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.setCancelable(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
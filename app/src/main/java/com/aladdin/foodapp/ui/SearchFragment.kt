package com.aladdin.foodapp.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.aladdin.foodapp.R
import com.aladdin.foodapp.adapters.MyAdapter
import com.aladdin.foodapp.databinding.FragmentSearchBinding
import com.aladdin.foodapp.models.FoodHome
import com.aladdin.foodapp.utils.Status
import com.aladdin.foodapp.viewmodel.ViewModel


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val arrayList1 = ArrayList<FoodHome>()
        setProgress()

        var a = 0
        val viewModel = ViewModelProvider(this)[ViewModel::class.java]
        viewModel.getHomeFood(binding.root.context).observe(viewLifecycleOwner) {

            when (it.status) {
                Status.SUCCESS -> {

                    dialog.cancel()
                    val myAdapter = MyAdapter(it.data!!, object : MyAdapter.OnClick {
                        override fun click(
                            pos: Int,
                            imageView: ImageView,
                            star: ImageView,
                            textView: TextView,
                            ball: TextView
                        ) {

                            val detailIntent =
                                Intent(
                                    requireActivity(),
                                    AboutBurgerActivity::class.java
                                ).putExtra("burger", it.data[pos])
                            val imageViewPair =
                                androidx.core.util.Pair(imageView as View, "small")
                            val starPair = androidx.core.util.Pair(star as View, "star_icon")
                            val ballPair = androidx.core.util.Pair(star as View, "ball")

                            val textViewPair =
                                androidx.core.util.Pair.create<View, String>(
                                    textView as View,
                                    "burger_name"
                                )
                            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                requireActivity(),
                                imageViewPair,
                                starPair,
                                ballPair
                            )
                            //detailIntent.putExtra(TestActivity.DATA, item)
                            closeKeyboard()
                            startActivity(detailIntent, options.toBundle())
                        }
                    })
                    binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)

                            println("kakaka: $dy")
                            if (dy > 10 || dy < 0) {
                                closeKeyboard()
                            }
                            /*  if (dy < 10){
                                  showKeyboard(binding.search)
                              }*/

                        }

                    })

                    binding.search.addTextChangedListener {
                        myAdapter.filter(it.toString())
                    }
                    binding.rv.adapter = myAdapter

                }
                Status.LOADING -> {}
                Status.ERROR -> {

                    dialog.cancel()

                }
            }

        }



        showKeyboard(binding.search)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showKeyboard(editText: EditText) {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        editText.requestFocus()
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun closeKeyboard() {

        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)

    }


    private lateinit var dialog: AlertDialog

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


}
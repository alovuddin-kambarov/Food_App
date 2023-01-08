package com.uzcoder.foodapp.ui

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
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.uzcoder.foodapp.adapters.MyAdapter
import com.uzcoder.foodapp.databinding.FragmentSearchBinding
import com.uzcoder.foodapp.utils.MyData


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val myAdapter = MyAdapter(MyData.getArraylist(), object : MyAdapter.OnClick {
            override fun click(
                pos: Int,
                imageView: ImageView,
                star: ImageView,
                textView: TextView,
                ball: TextView
            ) {
                /* val fragmentNavigatorExtras = FragmentNavigatorExtras(imageView to "big")
                 findNavController().navigate(R.id.aboutBurgerFragment,null,null,fragmentNavigatorExtras)*/

                val detailIntent =
                    Intent(
                        requireActivity(),
                        AboutBurgerActivity::class.java
                    ).putExtra("burger", MyData.getArraylist()[pos])
                val imageViewPair = androidx.core.util.Pair(imageView as View, "small")
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
                startActivity(detailIntent, options.toBundle())
            }
        })

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                println("kakaka: $dy")
                if (dy > 10 || dy < 0 ){
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

}
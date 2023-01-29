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
import com.aladdin.foodapp.adapters.MyAdapter
import com.aladdin.foodapp.databinding.FragmentBurgerBinding
import com.aladdin.foodapp.models.FoodHome
import com.aladdin.foodapp.utils.MyData
import com.aladdin.foodapp.utils.Status
import com.aladdin.foodapp.viewmodel.ViewModel

class BurgerFragment(var pos: String) : Fragment() {

    private lateinit var myAdapter: MyAdapter
    private var _binding: FragmentBurgerBinding? = null

    private val binding get() = _binding!!
    lateinit var arrayList1: ArrayList<FoodHome>

    lateinit var viewModel: ViewModel
    private lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBurgerBinding.inflate(inflater, container, false)



             setProgress()
            Handler(Looper.myLooper()!!).postDelayed({
                try {
                    viewModel = ViewModelProvider(this)[ViewModel::class.java]
                    arrayList1 = ArrayList<FoodHome>()
                    var a = 0
                    viewModel.getHomeFood(binding.root.context).observe(viewLifecycleOwner) {

                        when (it.status) {
                            Status.SUCCESS -> {

                                dialog.cancel()
                                binding.rv.visibility = View.VISIBLE
                                // binding.animationViews.visibility = View.GONE
                                if (a == 0) {

                                    val arrayList = it.data!!

                                    for (burger in arrayList.indices) {

                                        if (pos == arrayList[burger].category) {

                                            arrayList1.add(arrayList[burger])
                                        }

                                    }
                                    myAdapter = MyAdapter(arrayList1, object : MyAdapter.OnClick {
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
                                                ).putExtra("burger", arrayList1[pos])
                                            val imageViewPair =
                                                androidx.core.util.Pair(imageView as View, "small")
                                            val starPair =
                                                androidx.core.util.Pair(star as View, "star_icon")
                                            val ballPair = androidx.core.util.Pair(star as View, "ball")

                                            val textViewPair =
                                                androidx.core.util.Pair.create<View, String>(
                                                    textView as View,
                                                    "burger_name"
                                                )
                                            val options =
                                                ActivityOptionsCompat.makeSceneTransitionAnimation(
                                                    requireActivity(),
                                                    imageViewPair,
                                                    starPair,
                                                    ballPair
                                                )
                                            //detailIntent.putExtra(TestActivity.DATA, item)
                                            startActivity(detailIntent, options.toBundle())
                                        }
                                    })
                                    binding.rv.adapter = myAdapter
                                }
                                a++
                            }
                            Status.LOADING -> {}
                            Status.ERROR -> {
                                dialog.cancel()
                                binding.rv.visibility = View.GONE
                                //          binding.animationViews.visibility = View.VISIBLE
                            }
                        }

                    }
                } catch (e: Exception) {
                }


            }, 500)



        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
        //    binding.rv.smoothScrollToPosition(0)
/*
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView, state: Int) {
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    //  PostList.add(0, post)
                    //mAdapter.notifyItemInserted(0)
                    rv.removeOnScrollListener(this)
                }
            }
        })
        binding.rv.smoothScrollToPosition(0)
*/
    }


    private fun setProgress() {
        dialog = AlertDialog.Builder(binding.root.context).create()
        val view = LayoutInflater.from(binding.root.context)
            .inflate(com.aladdin.foodapp.R.layout.custom_progress, null, false)
        dialog.setView(view)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }


}
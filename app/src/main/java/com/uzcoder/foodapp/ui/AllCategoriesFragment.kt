package com.uzcoder.foodapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.uzcoder.foodapp.adapters.CategoryAdapter
import com.uzcoder.foodapp.databinding.FragmentAllCattigoriesBinding
import com.uzcoder.foodapp.utils.MySharedPreference

class AllCategoriesFragment : Fragment() {

    private var _binding: FragmentAllCattigoriesBinding? = null

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

        binding.rv.adapter = CategoryAdapter(categoryList, object : CategoryAdapter.OnClick {
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


        return binding.root
    }


    private fun loadCategory() {
        categoryList = ArrayList()
        categoryList.add("\uD83C\uDF54  Burger")
        categoryList.add("\uD83C\uDF55  Pitsa")
        categoryList.add("\uD83C\uDF2D  Hot-Dog")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
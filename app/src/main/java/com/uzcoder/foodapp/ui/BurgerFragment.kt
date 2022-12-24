package com.uzcoder.foodapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.uzcoder.foodapp.adapters.MyAdapter
import com.uzcoder.foodapp.databinding.FragmentBurgerBinding
import com.uzcoder.foodapp.models.Burger


class BurgerFragment(var pos: Short) : Fragment() {

    private var _binding: FragmentBurgerBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBurgerBinding.inflate(inflater, container, false)


        val arrayList = ArrayList<Burger>()



        arrayList.add(
            Burger(
                "https://pngimg.com/uploads/burger_sandwich/burger_sandwich_PNG4135.png",
                "Burger",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "20000",
                "3.4",
                0
            )
        )
        arrayList.add(
            Burger(
                "https://www.pngfind.com/pngs/m/74-743438_grill-burger-png-bacon-cheeseburger-png-transparent-png.png",
                "Big Burger",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "22000",
                "3.4",
                0
            )
        )
        arrayList.add(
            Burger(
                "https://www.citypng.com/public/uploads/preview/meat-burger-fast-food-illustration-png-image-11653074037kwxlikntuv.png",
                "Super Burger",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "24000",
                "3.4",
                0
            )
        )
        arrayList.add(
            Burger(
                "https://pngimg.com/uploads/burger_sandwich/burger_sandwich_PNG4157.png",
                "Burger",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "22000",
                "3.4",
                0
            )
        )
        arrayList.add(
            Burger(
                "https://parspng.com/wp-content/uploads/2022/05/pizzapng.parspng.com-8.png",
                "Pitsa Italiano",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "22000",
                "3.4",
                1
            )
        )
        arrayList.add(
            Burger(
                "https://pngimg.com/uploads/pizza/pizza_PNG43981.png",
                "Pitsa americano",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "22000",
                "3.4",
                1
            )
        )
        arrayList.add(
            Burger(
                "https://upload.wikimedia.org/wikipedia/commons/3/39/Supreme_pizza.png",
                "Pitsa Uzbekino",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "22000",
                "3.4",
                1
            )
        )

        arrayList.add(
            Burger(
                "https://pngimg.com/uploads/hot_dog/hot_dog_PNG10217.png",
                "Hot-dog simple",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "10000",
                "3.4",
                2
            )
        )

        arrayList.add(
            Burger(
                "https://i.pinimg.com/originals/d5/85/2f/d5852fcbfcc5a14c662d11eb1e5da1fe.png",
                "Hot-dog super",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "19000",
                "3.4",
                2
            )
        )



        arrayList.add(
            Burger(
                "https://www.seekpng.com/png/detail/12-125885_free-png-hot-dog-png-images-transparent-hot.png",
                "Hot-dog super mega",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "21000",
                "3.4",
                2
            )
        )


        arrayList.add(
            Burger(
                "https://www.freepnglogos.com/uploads/hot-dog-png/hot-dog-icon-noto-emoji-food-drink-iconset-google-38.png",
                "Hot dog simple",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "12000",
                "3.4",
                2
            )
        )


        arrayList.add(
            Burger(
                "https://www.seekpng.com/png/detail/186-1867076_hot-dog-bun-60-g-chicago-style-hot.png",
                "Hot-dog of students",
                "Piyoz, kartoshka, kechup mayanez, katlet",
                "4000",
                "3.4",
                2
            )
        )


        val arrayList1 = ArrayList<Burger>()

        for (burger in arrayList.indices) {

            if (pos == arrayList[burger].type) {

                arrayList1.add(arrayList[burger])
            }

        }


        binding.rv.adapter = MyAdapter(arrayList1, object : MyAdapter.OnClick {
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



        return binding.root
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

}
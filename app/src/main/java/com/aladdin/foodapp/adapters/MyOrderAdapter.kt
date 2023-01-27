package com.aladdin.foodapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aladdin.foodapp.databinding.ItemBurgerBinding
import com.aladdin.foodapp.models.OrderReq
import com.google.android.material.snackbar.Snackbar


class MyOrderAdapter(private var list: List<OrderReq>) :
    RecyclerView.Adapter<MyOrderAdapter.ViewH>() {

    var list2 = ArrayList<OrderReq>()

    init {

        list2 = list as ArrayList<OrderReq>

    }

    inner class ViewH(var binding: ItemBurgerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun onBind(item: OrderReq) {

            var a = 0

            binding.name.text = item.foods[0]
            binding.ball.text = "3.4"
            binding.price.text = item.price
            // binding.subText.text = item.subTitle
            // Picasso.get().load(item.image).into(binding.image)

            binding.addBasket.setOnClickListener {
                a++

                //if (a == 1) {
                item.count = a.toString()

                //}
                /*    item.count = a
                    AppDatabase.getInstants(binding.root.context).dao().update(item)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe()*/

                binding.count.visibility = View.VISIBLE
                binding.minus.visibility = View.VISIBLE

                binding.count.animationDuration = 200
                binding.count.setText(a.toString())





                Snackbar.make(binding.root, "Savatga qo'shildi!", 1000).show()
            }


            binding.minus.setOnClickListener {

                a--
                binding.count.setText(a.toString())

                if (a < 1) {
                    binding.minus.visibility = View.GONE
                    binding.count.visibility = View.GONE
                }

                println("haaaa: ${item.count}")

            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {
        return ViewH(
            ItemBurgerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewH, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


    interface OnClick {
        fun click(
            pos: Int,
            imageView: ImageView,
            star: ImageView,
            textView: TextView,
            ball: TextView
        )
    }
}



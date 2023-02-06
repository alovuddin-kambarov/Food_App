package com.aladdin.foodapp.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aladdin.foodapp.R
import com.aladdin.foodapp.databinding.ItemBurgerBinding
import com.aladdin.foodapp.models.FoodHome
import com.aladdin.foodapp.room.AppDatabase
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MyAdapter(private var list: List<FoodHome>, val onClick: OnClick) :
    RecyclerView.Adapter<MyAdapter.ViewH>() {

    var list2 = ArrayList<FoodHome>()

    init {

        list2 = list as ArrayList<FoodHome>

    }

    inner class ViewH(var binding: ItemBurgerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun onBind(item: FoodHome) {





            var a = 0

            binding.name.text = item.name
            binding.ball.text = "3.4"
            binding.price.text = item.price
            // binding.subText.text = item.subTitle
            Picasso.get().load(item.image).into(binding.image)

            if (item.visible == "g"){
                binding.name.paintFlags = binding.name.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }else{
                binding.name.setTypeface(null, Typeface.BOLD)
            }

            if (item.name.contains("combo") || item.name.contains("Combo")  ){
                binding.newImage.visibility = View.VISIBLE
            }else{
                binding.newImage.visibility = View.GONE
            }


            binding.addBasket.setOnClickListener {
                a++

                //if (a == 1) {
                item.count = a
                AppDatabase.getInstants(binding.root.context).dao().add(item)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe()

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
                    AppDatabase.getInstants(binding.root.context).dao().delete(item)
                    binding.minus.visibility = View.GONE
                    binding.count.visibility = View.GONE
                }

                item.count = a

                AppDatabase.getInstants(binding.root.context).dao().add(item)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe()

                println("haaaa: ${item.count}")

            }

            binding.click.setOnClickListener {
                onClick.click(
                    adapterPosition,
                    binding.image,
                    binding.starIcon,
                    binding.name,
                    binding.ball,
                    item
                )
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

        val loadAnimation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recy_anim)
        holder.itemView.startAnimation(loadAnimation)
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun filter(str: String) {
        list = ArrayList()
        for (i in list2.indices) {
            if (list2[i].name?.toLowerCase()?.contains(str.toLowerCase())!!) {
                (list as ArrayList<FoodHome>).add(list2[i])
            }
        }

        notifyDataSetChanged()

    }

    interface OnClick {
        fun click(
            pos: Int,
            imageView: ImageView,
            star: ImageView,
            textView: TextView,
            ball: TextView,
            burger:FoodHome
        )
    }


}
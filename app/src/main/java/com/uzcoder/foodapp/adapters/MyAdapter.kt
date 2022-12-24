package com.uzcoder.foodapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.uzcoder.foodapp.databinding.ItemBurgerBinding
import com.uzcoder.foodapp.models.Burger
import com.uzcoder.foodapp.room.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MyAdapter(private val list: List<Burger>, val onClick: OnClick) :
    RecyclerView.Adapter<MyAdapter.ViewH>() {

    inner class ViewH(var binding: ItemBurgerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun onBind(item: Burger) {

            binding.name.text = item.name
            binding.ball.text = item.star
            binding.price.text = item.price
            binding.subText.text = item.subTitle
            Picasso.get().load(item.image).into(binding.image)

            binding.addBasket.setOnClickListener {
                AppDatabase.getInstants(binding.root.context).dao().add(item)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe()

                Snackbar.make(binding.root,"Savatga qo'shildi!",1000).show()
            }


            binding.root.setOnClickListener {
                onClick.click(
                    adapterPosition,
                    binding.image,
                    binding.starIcon,
                    binding.name,
                    binding.ball
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
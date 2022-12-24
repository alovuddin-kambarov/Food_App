package com.uzcoder.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.uzcoder.foodapp.databinding.ItemBurgerBinding
import com.uzcoder.foodapp.models.Burger
import com.uzcoder.foodapp.room.AppDatabase

class MyAdapterBasket :
    ListAdapter<Burger, MyAdapterBasket.ViewH>(MyDiffUtil()) {

    inner class ViewH(var binding: ItemBurgerBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun onBind(burger: Burger) {

            binding.name.text = burger.name
            binding.ball.text = burger.star
            binding.price.text = burger.price
            binding.subText.text = burger.subTitle
            Picasso.get().load(burger.image).into(binding.image)

            binding.addBasket.setOnClickListener {
                AppDatabase.getInstants(binding.root.context).dao().delete(burger)

                Snackbar.make(binding.root,"Savatdan o'chirildi!",1000).show()
            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {

        return ViewH(ItemBurgerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }




    class MyDiffUtil : DiffUtil.ItemCallback<Burger>() {
        override fun areItemsTheSame(oldItem: Burger, newItem: Burger): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Burger, newItem: Burger): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onBindViewHolder(holder: ViewH, position: Int) {
        holder.onBind(getItem(position))
    }

}


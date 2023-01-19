package com.aladdin.foodapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.aladdin.foodapp.databinding.ItemBurgerBinding
import com.aladdin.foodapp.models.FoodHome
import com.aladdin.foodapp.room.AppDatabase

class MyAdapterBasket :
    ListAdapter<FoodHome, MyAdapterBasket.ViewH>(MyDiffUtil()) {

    inner class ViewH(var binding: ItemBurgerBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun onBind(burger: FoodHome) {

            binding.name.text = burger.name
            binding.ball.text = "3.4"
            binding.count.visibility = View.VISIBLE
            binding.count.setText(burger.count.toString())
            binding.price.text = burger.price
            //binding.subText.text = burger.subTitle
            Picasso.get().load(burger.image).into(binding.image)

            if (burger.count == 0){
                    AppDatabase.getInstants(binding.root.context).dao().delete(burger)
            }

            binding.addBasket.setOnClickListener {
                AppDatabase.getInstants(binding.root.context).dao().delete(burger)

                Snackbar.make(binding.root,"Savatdan o'chirildi!",1000).show()
            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {

        return ViewH(ItemBurgerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }




    class MyDiffUtil : DiffUtil.ItemCallback<FoodHome>() {
        override fun areItemsTheSame(oldItem: FoodHome, newItem: FoodHome): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FoodHome, newItem: FoodHome): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onBindViewHolder(holder: ViewH, position: Int) {
        holder.onBind(getItem(position))
    }

}

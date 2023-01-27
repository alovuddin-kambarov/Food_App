package com.aladdin.foodapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aladdin.foodapp.databinding.ItemBurgerBinding
import com.aladdin.foodapp.models.FoodHome
import com.aladdin.foodapp.room.AppDatabase
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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


            var a = burger.count

            binding.count.visibility = View.VISIBLE
            binding.minus.visibility = View.VISIBLE
            binding.addBasket.setOnClickListener {
                a++

                //if (a == 1) {
                burger.count = a
                AppDatabase.getInstants(binding.root.context).dao().add(burger)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
                //}
                /*    burger.count = a
                    AppDatabase.getInstants(binding.root.context).dao().update(burger)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe()*/


                binding.count.animationDuration = 200
                binding.count.setText(a.toString())





                Snackbar.make(binding.root, "Savatga qo'shildi!", 1000).show()
            }


            binding.minus.setOnClickListener {

                a--
                binding.count.setText(a.toString())

                if (a < 1) {
                    AppDatabase.getInstants(binding.root.context).dao().delete(burger)
                    binding.minus.visibility = View.GONE
                    binding.count.visibility = View.GONE
                }

                burger.count = a

                AppDatabase.getInstants(binding.root.context).dao().add(burger)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe()

                println("haaaa: ${burger.count}")

            }

/*

            binding.addBasket.setOnClickListener {

                AppDatabase.getInstants(binding.root.context).dao().delete(burger)

                Snackbar.make(binding.root,"Savatdan o'chirildi!",1000).show()
            }
*/



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


package com.aladdin.foodapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aladdin.foodapp.R
import com.aladdin.foodapp.databinding.CategoryItemBinding
import com.aladdin.foodapp.models.Category
import com.squareup.picasso.Picasso


class CategoryAdapter(private val list: List<Category>, val onClick: OnClick) : RecyclerView.Adapter<CategoryAdapter.ViewH>() {

    inner class ViewH(var binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun onBind(item: Category) {

            itemView.setOnClickListener {
                onClick.click(adapterPosition,binding.image, binding.image,binding.name,binding.name,item.name)
            }
            binding.name.text = item.name
            Picasso.get().load(item.image).into(binding.image)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {
        return ViewH(
            CategoryItemBinding.inflate(
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

    interface OnClick{
        fun click(pos:Int, imageView: ImageView, star: ImageView, textView: TextView, ball: TextView,name:String)
    }

}
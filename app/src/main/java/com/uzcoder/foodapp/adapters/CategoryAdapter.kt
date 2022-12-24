package com.uzcoder.foodapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uzcoder.foodapp.databinding.CategoryItemBinding


class CategoryAdapter(private val list: List<String>, val onClick: OnClick) : RecyclerView.Adapter<CategoryAdapter.ViewH>() {

    inner class ViewH(var binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun onBind(item: String) {

            itemView.setOnClickListener {
                onClick.click(adapterPosition,binding.image, binding.image,binding.name,binding.name)
            }
            binding.name.text = item

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
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClick{
        fun click(pos:Int, imageView: ImageView, star: ImageView, textView: TextView, ball: TextView)
    }

}
package com.uzcoder.foodapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.uzcoder.foodapp.ui.BurgerFragment

class ViewPagerAdapter2(fa: Fragment, var list: List<String>) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return BurgerFragment(position.toShort())
    }

}
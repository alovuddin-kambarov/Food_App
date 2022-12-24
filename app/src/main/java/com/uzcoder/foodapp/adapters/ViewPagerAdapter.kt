package com.uzcoder.foodapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.uzcoder.foodapp.ui.DeliveryFragment
import com.uzcoder.foodapp.ui.HomeFragment
import com.uzcoder.foodapp.ui.SettingsFragment

class ViewPagerAdapter(fa: Fragment) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return HomeFragment()
            1 -> return DeliveryFragment()
            2 -> return SettingsFragment()
        }
        return HomeFragment()
    }

}
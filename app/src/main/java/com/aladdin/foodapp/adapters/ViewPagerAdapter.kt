package com.aladdin.foodapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aladdin.foodapp.ui.DeliveryFragment
import com.aladdin.foodapp.ui.HomeFragment
import com.aladdin.foodapp.ui.SettingsFragment

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
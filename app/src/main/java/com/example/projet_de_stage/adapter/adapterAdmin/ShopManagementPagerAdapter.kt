package com.example.projet_de_stage.adapter.adapterAdmin

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.fragment.fragmentAdmin.AppointmentsFragment
import com.example.projet_de_stage.fragment.fragmentAdmin.JoinRequestsFragment
import com.example.projet_de_stage.fragment.fragmentAdmin.MyShopsFragment

/**
 * Adapter for managing admin fragments in ViewPager2.
 * Displays tabs: Appointments, Join Requests, and My Shops.
 *
 * @param activity The parent FragmentActivity hosting the ViewPager2.
 * @param shopOwner The currently logged-in shop owner (used to pass UID to fragments).
 */
class ShopManagementPagerAdapter(
    activity: FragmentActivity,
    private val shopOwner: ShopOwner
) : FragmentStateAdapter(activity) {

    /**
     * Returns the number of fragments (pages) in the ViewPager2.
     */
    override fun getItemCount(): Int = 3

    /**
     * Creates the appropriate Fragment for the given position.
     * Passes the shop owner's UID via arguments to each fragment.
     *
     * @param position Index of the tab: 0 = Appointments, 1 = Join Requests, 2 = My Shops.
     * @return The fragment corresponding to the tab.
     */
    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment
        val bundle = Bundle().apply {
            putString("shopOwner", shopOwner.uid)
        }

        fragment = when (position) {
            0 -> AppointmentsFragment()
            1 -> JoinRequestsFragment()
            2 -> MyShopsFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }

        fragment.arguments = bundle
        return fragment
    }
}

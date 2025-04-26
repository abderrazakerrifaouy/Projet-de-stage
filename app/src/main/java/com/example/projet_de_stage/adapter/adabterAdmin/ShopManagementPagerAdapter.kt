package com.example.projet_de_stage.adapter.adabterAdmin

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.fragment.fragmentAdmin.AppointmentsFragment
import com.example.projet_de_stage.fragment.fragmentAdmin.JoinRequestsFragment
import com.example.projet_de_stage.fragment.fragmentAdmin.MyShopsFragment

class ShopManagementPagerAdapter(activity: FragmentActivity , private val shopOwner: ShopOwner) : FragmentStateAdapter(activity) {

    // عدد الصفحات (الفراجمنتات) اللي غادي يكونو فـ ViewPager
    override fun getItemCount(): Int = 3

    // هاد الميثود كتحدد شنو الفراجمنت اللي غادي يبان فكل تبويبة حسب البوزيسيون
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = AppointmentsFragment()
                val bundle = Bundle()
                bundle.putString("shopOwner", shopOwner.uid.toString())
                fragment.arguments = bundle
                fragment
            }
            // التبويبة ديال المواعيد
            1 -> {
                val fragment = JoinRequestsFragment()
                val bundle = Bundle()
                bundle.putString("shopOwner", shopOwner.uid.toString())
                fragment.arguments = bundle
                fragment
            }
            // التبويبة ديال طلبات الانضمام
            2 ->{
                val fragment = MyShopsFragment()
                val bundle = Bundle()
                bundle.putString("shopOwner", shopOwner.uid.toString())
                fragment.arguments = bundle
                fragment
            }           // التبويبة ديال محلاتي
            else -> throw IllegalStateException("الموقع غير صحيح: $position")
        }
    }
}
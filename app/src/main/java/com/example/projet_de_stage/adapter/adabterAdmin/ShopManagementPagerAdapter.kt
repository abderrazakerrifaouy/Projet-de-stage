package com.example.projet_de_stage.adapter.adabterAdmin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projet_de_stage.fragment.fragmentAdmin.AppointmentsFragment
import com.example.projet_de_stage.fragment.fragmentAdmin.JoinRequestsFragment
import com.example.projet_de_stage.fragment.fragmentAdmin.MyShopsFragment

class ShopManagementPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // عدد الصفحات (الفراجمنتات) اللي غادي يكونو فـ ViewPager
    override fun getItemCount(): Int = 3

    // هاد الميثود كتحدد شنو الفراجمنت اللي غادي يبان فكل تبويبة حسب البوزيسيون
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AppointmentsFragment()     // التبويبة ديال المواعيد
            1 -> JoinRequestsFragment()     // التبويبة ديال طلبات الانضمام
            2 -> MyShopsFragment()          // التبويبة ديال محلاتي
            else -> throw IllegalStateException("الموقع غير صحيح: $position")
        }
    }
}
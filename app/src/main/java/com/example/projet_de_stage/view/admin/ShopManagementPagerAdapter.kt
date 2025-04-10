package com.example.projet_de_stage.view.admin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projet_de_stage.fragment.AppointmentsFragment
import com.example.projet_de_stage.fragment.JoinRequestsFragment
import com.example.projet_de_stage.fragment.MyShopsFragment
import com.example.projet_de_stage.fragment.RatingsFragment

// هذا الملف يحتوي على Adapter خاص بـ ViewPager2 لعرض التبويبات الأربعة

class ShopManagementPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // عدد الصفحات (الفراجمنتات) اللي غادي يكونو فـ ViewPager
    override fun getItemCount(): Int = 4

    // هاد الميثود كتحدد شنو الفراجمنت اللي غادي يبان فكل تبويبة حسب البوزيسيون
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AppointmentsFragment()     // التبويبة ديال المواعيد
            1 -> JoinRequestsFragment()     // التبويبة ديال طلبات الانضمام
            2 -> MyShopsFragment()          // التبويبة ديال محلاتي
            3 -> RatingsFragment()          // التبويبة ديال التقييمات
            else -> throw IllegalStateException("الموقع غير صحيح: $position")
        }
    }
}

package com.example.projet_de_stage.adapter.adabterBarber

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projet_de_stage.fragment.fragmentBarber.AcceptedRequestsFragment
import com.example.projet_de_stage.fragment.fragmentBarber.HistoryRequestsFragment
import com.example.projet_de_stage.fragment.fragmentBarber.NewRequestsFragment

class AppointmentsManagerAdapter (activity : FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int  = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewRequestsFragment()     // التبويبة ديال المواعيد
            1 -> AcceptedRequestsFragment()     // التبويبة ديال طلبات الانضمام
            2 -> HistoryRequestsFragment()          // التبويبة ديال محلاتي
            else -> throw IllegalStateException("الموقع غير صحيح: $position")
        }
    }


}
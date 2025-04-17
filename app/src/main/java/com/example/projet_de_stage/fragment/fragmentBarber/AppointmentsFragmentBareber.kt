package com.example.projet_de_stage.fragment.fragmentBarber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adabterBarber.AppointmentsManagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AppointmentsFragmentBareber : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_barber_appointments, container, false)
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adaptere = AppointmentsManagerAdapter(requireActivity())
        viewPager.adapter = adaptere

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "طلبات جديدة"
                1 -> tab.text = "طلبات المقبولة"
                2 -> tab.text = "سجل طلبات"
            }

        }.attach()
    }
}
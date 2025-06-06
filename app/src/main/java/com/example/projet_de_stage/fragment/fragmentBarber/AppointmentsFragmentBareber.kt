package com.example.projet_de_stage.fragment.fragmentBarber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterBarber.AppointmentsManagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Fragment to manage and display barber appointment requests in a tab layout.
 */
class AppointmentsFragmentBarber : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    /**
     * Called when the fragment's view is being created. Initializes TabLayout and ViewPager2.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_barber_appointments, container, false)
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)
        return view
    }

    /**
     * Called when the view has been created. Sets up the adapter for ViewPager2
     * and attaches it to the TabLayout.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setting up the ViewPager2 adapter with the barber object passed via arguments
        val adapter = AppointmentsManagerAdapter(
            requireActivity(),
            arguments?.getParcelable("barber") // Pass barber object to the adapter
        )
        viewPager.adapter = adapter

        // Attach the TabLayout with ViewPager2 and set tab titles for each position
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.new_requests)
                1 -> tab.text = getString(R.string.accepted_requests)
                2 -> tab.text = getString(R.string.request_history)
            }
        }.attach()
    }
}

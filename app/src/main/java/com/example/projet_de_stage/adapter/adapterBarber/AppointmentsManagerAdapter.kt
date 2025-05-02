package com.example.projet_de_stage.adapter.adapterBarber

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.fragment.fragmentBarber.AcceptedRequestsFragment
import com.example.projet_de_stage.fragment.fragmentBarber.HistoryRequestsFragment
import com.example.projet_de_stage.fragment.fragmentBarber.NewRequestsFragment

/**
 * Adapter used with ViewPager2 to manage three appointment-related fragments for a Barber:
 * - New Requests
 * - Accepted Requests
 * - Appointment History
 *
 * @param activity The parent activity hosting the ViewPager2.
 * @param barber The barber instance to pass to each fragment.
 */
class AppointmentsManagerAdapter(
    activity: FragmentActivity,
    private val barber: Barber?
) : FragmentStateAdapter(activity) {

    /**
     * Number of pages/fragments managed by this adapter.
     */
    override fun getItemCount(): Int = 3

    /**
     * Returns the appropriate Fragment based on the current position.
     *
     * @param position The index of the tab/page.
     */
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewRequestsFragment.newInstance(barber)        // Tab: New Requests
            1 -> AcceptedRequestsFragment.newInstance(barber)   // Tab: Accepted Appointments
            2 -> HistoryRequestsFragment.newInstance(barber)    // Tab: History
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}

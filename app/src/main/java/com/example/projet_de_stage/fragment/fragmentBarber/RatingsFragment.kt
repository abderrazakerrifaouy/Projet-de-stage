package com.example.projet_de_stage.fragment.fragmentBarber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterBarber.RatingsAdapter
import com.example.projet_de_stage.data.Rating

/**
 * Fragment to display the ratings for the barber.
 */
class RatingsFragment : Fragment() {

    /**
     * Called when the fragment view is created. Sets up RecyclerView and UI components.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the fragment layout
        val view = inflater.inflate(R.layout.fragment_ratings, container, false)

        // Initialize RecyclerView and the list of ratings
        val recyclerView = view.findViewById<RecyclerView>(R.id.ratingsRecyclerView)

        // Sample data for the ratings
        val ratings = listOf(
            Rating("1", "Men's Salon", "Mohamed", "5.0", "Excellent service", "2023-05-15"),
            Rating("2", "Men's Salon", "Ali", "4.5", "Good service", "2023-05-14"),
            Rating("3", "Men's Salon", "Sara", "5.0", "Very satisfied", "2023-05-13"),
            Rating("4", "Men's Salon", "Mona", "4.0", "Nice experience", "2023-05-12"),
            Rating("5", "Men's Salon", "Ahmed", "5.0", "Highly recommended", "2023-05-11")
        )

        // Set up the adapter and layout manager for the RecyclerView
        recyclerView.adapter = RatingsAdapter(ratings)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }
}

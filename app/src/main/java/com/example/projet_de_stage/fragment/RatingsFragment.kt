package com.example.projet_de_stage.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.RatingsAdapter
import com.example.projet_de_stage.data.Rating


class RatingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_ratings, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.ratingsRecyclerView)
        val ratings = listOf(
            Rating("1", "صالون الرجال", "محمد", "5.0", "خدمة ممتازة", "2023-05-15"),
            Rating("1", "صالون الرجال", "محمد", "5.0", "خدمة ممتازة", "2023-05-15"),
            Rating("1", "صالون الرجال", "محمد", "5.0", "خدمة ممتازة", "2023-05-15"),
            Rating("1", "صالون الرجال", "محمد", "5.0", "خدمة ممتازة", "2023-05-15"),
            Rating("1", "صالون الرجال", "محمد", "5.0", "خدمة ممتازة", "2023-05-15"),
            Rating("1", "صالون الرجال", "محمد", "5.0", "خدمة ممتازة", "2023-05-15"),
            Rating("1", "صالون الرجال", "محمد", "5.0", "خدمة ممتازة", "2023-05-15"),
            Rating("1", "صالون الرجال", "محمد", "5.0", "خدمة ممتازة", "2023-05-15"),
            Rating("1", "صالون الرجال", "محمد", "5.0", "خدمة ممتازة", "2023-05-15"),
            Rating("1", "صالون الرجال", "محمد", "5.0", "خدمة ممتازة", "2023-05-15"),
            Rating("1", "صالون الرجال", "محمد", "5.0", "خدمة ممتازة", "2023-05-15"),
            Rating("1", "صالون الرجال", "محمد", "5.0", "خدمة ممتازة", "2023-05-15"),
            Rating("1", "صالون الرجال", "محمد", "5.0", "خدمة ممتازة", "2023-05-15"),
        )

        recyclerView.adapter = RatingsAdapter(ratings)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }
}

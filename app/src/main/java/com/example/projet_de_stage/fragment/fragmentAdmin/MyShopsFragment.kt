package com.example.projet_de_stage.fragment.fragmentAdmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adabterAdmin.ShopsAdapter
import com.example.projet_de_stage.data.Shop

class MyShopsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_shops, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.shopsRecyclerView)
        val shops = listOf(
            Shop("1", "صالون الرجال", "4.5", "(50 تقييم)", "safi", R.drawable.menupng),
            Shop("1", "صالون الرجال", "4.5", "(50 تقييم)", "safi", R.drawable.menupng),
            Shop("1", "صالون الرجال", "4.5", "(50 تقييم)", "safi", R.drawable.menupng),
        )

        recyclerView.adapter = ShopsAdapter(shops) { shopId ->
            Toast.makeText(requireContext(), "فتح إدارة المحل $shopId", Toast.LENGTH_SHORT).show()
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        return view
    }
}
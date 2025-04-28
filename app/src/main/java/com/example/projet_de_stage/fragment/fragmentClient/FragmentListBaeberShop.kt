package com.example.projet_de_stage.fragment.fragmentClient

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterClient.ShopAdapter
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.view.client.BarbershopDetails
import com.example.projet_de_stage.viewModel.ClientViewModel
import kotlinx.coroutines.launch
import kotlin.jvm.java

class FragmentListBarberShop : Fragment() {
    private val viewModel : ClientViewModel = ClientViewModel()
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_barber_shop, container, false)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.rvBarbershops)

        // Sample data with the updated Barber class
        lifecycle.coroutineScope.launch {
            val shopsList = viewModel.getShops()

            val adapter = ShopAdapter(shopsList) { shop ->
                val intent = Intent(requireContext(), BarbershopDetails::class.java)
                intent.putExtra("SHOP_DATA", shop)
                startActivity(intent)
                // For navigation to shop details:
                // findNavController().navigate(R.id.action_to_shopDetails, bundleOf("SHOP_DATA" to shop))
            }


            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
        // Optional: Add divider between items
        // recyclerView.addItemDecoration(
        //     DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
        //         setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
        //     }
        // )

        return view
    }
}
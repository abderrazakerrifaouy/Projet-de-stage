package com.example.projet_de_stage.fragment.fragmentClient

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterClient.ShopAdapter
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.view.client.BarbershopDetails
import com.example.projet_de_stage.viewModel.ClientViewModel
import kotlinx.coroutines.launch

/**
 * Fragment to list all the barbershops available for the customer.
 */
class FragmentListBarberShop : Fragment() {

    private val viewModel: ClientViewModel by viewModels()  // ViewModel to manage shop data
    private lateinit var recyclerView: RecyclerView  // RecyclerView to display the list of barbershops
    private var client: Customer? = null  // Holds the customer data passed to the fragment

    /**
     * Called to create the view for this fragment.
     * Sets up RecyclerView and loads the barbershop data.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_barber_shop, container, false)

        // Set up RecyclerView and load barbershop data
        setupRecyclerView(view)
        loadShops()

        return view
    }

    /**
     * Sets up the RecyclerView with a LinearLayoutManager.
     * This function configures the layout and assigns the RecyclerView to the layout.
     */
    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.rvBarbershops)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    /**
     * Loads the list of barbershops from the ViewModel and updates the RecyclerView.
     * It checks if the customer data is available.
     * If customer data is found, it populates the RecyclerView using ShopAdapter.
     * If customer data is missing, a Toast message is shown.
     */
    private fun loadShops() {
        viewLifecycleOwner.lifecycleScope.launch {
            client = arguments?.getParcelable("customer")

            // If the client data is not available, show a toast message
            if (client == null) {
                Toast.makeText(requireContext(), "Customer not found", Toast.LENGTH_SHORT).show()
                return@launch
            }

            // Get the list of shops from ViewModel
            val shopsList = viewModel.getShops()

            // Set up the adapter for RecyclerView
            val adapter = ShopAdapter(shopsList) { shop ->
                // Open BarbershopDetails activity when a shop is clicked
                val intent = Intent(requireContext(), BarbershopDetails::class.java)
                intent.putExtra("SHOP_DATA", shop)
                intent.putExtra("CLIENT_DATA", client)
                startActivity(intent)
            }

            // Set the adapter to the RecyclerView
            recyclerView.adapter = adapter
        }
    }
}

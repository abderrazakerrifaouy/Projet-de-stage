package com.example.projet_de_stage.fragment.fragmentClient

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterClient.ShopAdapter
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.view.client.BarbershopDetails
import kotlin.jvm.java

class FragmentListBarberShop : Fragment() {
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
        val shopsList = listOf(
            Shop(
                id = "1",
                name = "صالون الرجال الفاخر",
                rating = "4.8",
                reviews = "120",
                address = "الرياض، حي الملك فهد",
                imageRes = R.drawable.ic_barber, // Replace with your actual image
                barbers = listOf(
                    Barber(
                        id = "1",
                        name = "أحمد",
                        experience = "5 سنوات",
                        email = "ahmed@example.com",
                        phone = "0501234567",
                        address = "الرياض",
                        password = "password123",
                        shopId = "1",
                        imageRes = R.drawable.ic_barber
                    ),
                    Barber(
                        id = "2",
                        name = "محمد",
                        experience = "3 سنوات",
                        email = "mohamed@example.com",
                        phone = "0507654321",
                        address = "الرياض",
                        password = "password123",
                        shopId = "1",
                        imageRes = R.drawable.ic_barber
                    )
                )
            ),
            Shop(
                id = "2",
                name = "صالون الحلاقة المتميز",
                rating = "4.5",
                reviews = "85",
                address = "الرياض، حي النخيل",
                imageRes = R.drawable.ic_barber,
                barbers = listOf(
                    Barber(
                        id = "3",
                        name = "خالد",
                        experience = "7 سنوات",
                        email = "khaled@example.com",
                        phone = "0501112233",
                        address = "الرياض",
                        password = "password123",
                        shopId = "2",
                        imageRes = R.drawable.ic_barber,
                        rating = 2.5f
                    )
                )
            )
        )

        val adapter = ShopAdapter(shopsList) { shop ->
            val intent = Intent(requireContext(), BarbershopDetails::class.java)
            intent.putExtra("SHOP_DATA", shop)
            startActivity(intent)
            // For navigation to shop details:
            // findNavController().navigate(R.id.action_to_shopDetails, bundleOf("SHOP_DATA" to shop))
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Optional: Add divider between items
        // recyclerView.addItemDecoration(
        //     DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
        //         setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
        //     }
        // )

        return view
    }
}
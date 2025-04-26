package com.example.projet_de_stage.fragment.fragmentClient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Customer
import kotlin.apply

class ProfileFragmentClient : Fragment() {
    private lateinit var customer: Customer
    private lateinit var profileImage: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvAddress: TextView
    private lateinit var btnEditProfile: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_client, container, false)

        // Initialize views
        profileImage = view.findViewById(R.id.profileImage)
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvPhone = view.findViewById(R.id.tvPhone)
        tvAddress = view.findViewById(R.id.tvAddress)
        btnEditProfile = view.findViewById(R.id.btnEditProfile)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For testing, create a dummy customer
        customer = Customer(
            uid = "1",
            name = "محمد علي",
            email = "customer@example.com",
            phone = "+966501234567",
            address = "الرياض، حي النخيل",
            password = "password123",
            imageRes = R.drawable.my_profile
        )

        bindCustomerData()

        btnEditProfile.setOnClickListener {
            // Handle edit profile button click
        }
    }

    private fun bindCustomerData() {
        profileImage.setImageResource(customer.imageRes)
        tvName.text = customer.name
        tvEmail.text = customer.email
        tvPhone.text = customer.phone
        tvAddress.text = customer.address

        // Hide the experience TextView as it's not relevant for customers
        view?.findViewById<TextView>(R.id.tvExperience)?.visibility = View.GONE
    }

    companion object {
        fun newInstance(customer: Customer): ProfileFragmentClient {
            return ProfileFragmentClient().apply {
                arguments = Bundle().apply {
                    putParcelable("customer", customer)
                }
            }
        }
    }
}
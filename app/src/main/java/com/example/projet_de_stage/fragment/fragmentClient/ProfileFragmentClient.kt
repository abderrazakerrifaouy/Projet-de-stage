package com.example.projet_de_stage.fragment.fragmentClient

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.view.LoginActivity
import com.example.projet_de_stage.view.client.UpdateCustomerProfileActivity

/**
 * ProfileFragmentClient displays the profile information of a customer.
 * It allows the customer to edit their profile or log out.
 */
class ProfileFragmentClient : Fragment() {

    private lateinit var customer: Customer
    private lateinit var profileImage: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvAddress: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnLogOutClient: Button // Renamed to 'btnLogOutClient' for clarity

    /**
     * Inflate the fragment's layout and initialize the views.
     */
    @SuppressLint("MissingInflatedId")
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
        btnLogOutClient = view.findViewById(R.id.btnLogOutClient) // Fixed the button ID

        return view
    }

    /**
     * Bind the customer data to the views and set up the click listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For testing, create a dummy customer
        customer = arguments?.getParcelable("customer")!!

        bindCustomerData()

        // Set up the "Edit Profile" button to navigate to the update profile screen
        btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), UpdateCustomerProfileActivity::class.java)
            intent.putExtra("CUSTOMER_DATA", customer)
            startActivity(intent)
        }

        // Set up the "Log Out" button to clear the user data and navigate to the login screen
        btnLogOutClient.setOnClickListener {
            val prefs = requireContext().getSharedPreferences("prefs", MODE_PRIVATE)
            prefs.edit {
                putString("uid", "") // Clear the user's UID
            }
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Finish the current activity
        }
    }

    /**
     * Binds the customer data to the views.
     */
    private fun bindCustomerData() {
        profileImage.setImageResource(customer.imageRes)
        tvName.text = customer.name
        tvEmail.text = customer.email
        tvPhone.text = customer.phone
        tvAddress.text = customer.address

        // Hide the experience TextView as it's not relevant for customers
        view?.findViewById<TextView>(R.id.tvExperience)?.visibility = View.GONE
    }
}

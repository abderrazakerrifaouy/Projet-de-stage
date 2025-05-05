package com.example.projet_de_stage.fragment.fragmentBarber

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
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.view.LoginActivity
import com.example.projet_de_stage.viewModel.ClientViewModel
import androidx.core.content.edit
import com.bumptech.glide.Glide
import com.example.projet_de_stage.view.barberUser.ModifyBarberActivity

/**
 * ProfileFragment displays the barber's profile information and allows them to edit the profile or log out.
 */
class ProfileFragment : Fragment() {
    private lateinit var barber: Barber
    private lateinit var profileImage: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvExperience: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvAddress: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnLogOut: Button  // Button to log out the user

    /**
     * Called to create the fragment's view. Initializes UI components.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize the views
        profileImage = view.findViewById(R.id.profileImage)
        tvName = view.findViewById(R.id.tvName)
        tvExperience = view.findViewById(R.id.tvExperience)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvPhone = view.findViewById(R.id.tvPhone)
        tvAddress = view.findViewById(R.id.tvAddress)
        btnEditProfile = view.findViewById(R.id.btnEditProfile)
        btnLogOut = view.findViewById(R.id.btnLogAut) // Renaming to 'btnLogOut'

        return view
    }

    /**
     * Called after the fragment's view is created. Binds the barber's data to the UI and handles button actions.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get barber data from the arguments
        barber = arguments?.getParcelable<Barber>("barber") ?: return
        bindBarberData()

        // Set up the log out button
        btnLogOut.setOnClickListener {
            // Clear UID from SharedPreferences and navigate to the login screen
            val prefs = requireContext().getSharedPreferences("prefs", MODE_PRIVATE)
            prefs.edit {
                putString("uid", "")
            }

            // Navigate to LoginActivity and finish the current activity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // Set up the edit profile button
        btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), ModifyBarberActivity::class.java)
            intent.putExtra("barber", barber)
            startActivity(intent)
        }
    }

    /**
     * Binds the barber's data to the corresponding UI components.
     */
    @SuppressLint("SetTextI18n")
    private fun bindBarberData() {

        if (barber.imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(barber.imageUrl)
                .into(profileImage)
        } else {
            profileImage.setImageResource(R.drawable.my_profile)
        }


        tvName.text = barber.name
        tvExperience.text = "Experience: ${barber.experience} years"
        tvEmail.text = barber.email
        tvPhone.text = barber.phone
        // Optional: If you want to show address, uncomment the following line
        // tvAddress.text = barber.address
    }
}

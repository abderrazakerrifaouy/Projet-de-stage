package com.example.projet_de_stage.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.data.UserType
import com.google.android.material.textfield.TextInputEditText

/**
 * Fragment for inputting the user's full name (first name and last name).
 */
class NameInputFragment : Fragment() {
    private lateinit var nextButton: Button
    private lateinit var firstNameInput: TextInputEditText
    private lateinit var lastNameInput: TextInputEditText
    private lateinit var userTypeString: String
    private lateinit var userType: UserType
    private lateinit var fullName: String

    /**
     * Called when the fragment's view is created.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.full_name, container, false)

        // Initialize UI elements
        nextButton = view.findViewById(R.id.nextButton)
        firstNameInput = view.findViewById(R.id.firstNameInput)
        lastNameInput = view.findViewById(R.id.lastNameInput)

        // Retrieve user type from the arguments
        userTypeString = arguments?.getString("userType").toString()
        userType = UserType.valueOf(userTypeString)

        return view
    }

    /**
     * Called after the fragment's view is created and initialized.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = Bundle()

        // Set up click listener for the "Next" button
        nextButton.setOnClickListener {
            // Combine first and last name to form the full name
            fullName = "${firstNameInput.text} ${lastNameInput.text}"

            // Update the user object based on user type
            when (userType) {
                UserType.Barber -> {
                    val barber = arguments?.getParcelable("user") ?: Barber()
                    barber.name = fullName
                    bundle.putParcelable("user", barber)
                }
                UserType.Customer -> {
                    val customer = arguments?.getParcelable("user") ?: Customer()
                    customer.name = fullName
                    bundle.putParcelable("user", customer)
                }
                UserType.ShopOwner -> {
                    val shopOwner = arguments?.getParcelable("user") ?: ShopOwner()
                    shopOwner.name = fullName
                    bundle.putParcelable("user", shopOwner)
                }
            }

            // Pass the user type to the next fragment
            bundle.putString("userType", userTypeString)

            // Navigate to the next fragment (BirthDateFragment)
            val birthDateFragment = BirthDateFragment()
            birthDateFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,  // Slide in animation
                    R.anim.fade_out,  // Fade out animation
                    R.anim.fade_in,   // Fade in animation when returning
                    R.anim.slide_out  // Slide out animation when returning
                )
                .replace(R.id.fragment_container, birthDateFragment)
                .addToBackStack("NameInputFragment") // Allow going back with the back button
                .commit()
        }
    }
}

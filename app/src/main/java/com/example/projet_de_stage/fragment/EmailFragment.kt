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
 * Fragment for inputting the phone number (or email) during user registration.
 */
class EmailFragment : Fragment() {
    private lateinit var nextButton: Button
    private lateinit var emailInput: TextInputEditText
    private lateinit var userTypeString: String
    private lateinit var userType: UserType

    /**
     * Called when the fragment's view is created.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.phone_number, container, false)

        // Initialize the UI elements
        nextButton = view.findViewById(R.id.nextButton)
        emailInput = view.findViewById(R.id.phoneInput)

        // Get the user type passed from the previous fragment
        userTypeString = arguments?.getString("userType").toString()
        userType = UserType.valueOf(userTypeString)

        return view
    }

    /**
     * Called after the fragment's view is created and initialized.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the "Next" button click listener
        nextButton.setOnClickListener {
            val bundle = Bundle()

            // Get the email (or phone number) entered by the user
            val emailText = emailInput.text.toString()

            // Add user type to the bundle
            bundle.putString("userType", userType.name)

            // Handle the user type and store the email
            when (userType) {
                UserType.Barber -> {
                    val barber = arguments?.getParcelable("user") ?: Barber()
                    barber.email = emailText
                    bundle.putParcelable("user", barber)
                }
                UserType.Customer -> {
                    val customer = arguments?.getParcelable("user") ?: Customer()
                    customer.email = emailText
                    bundle.putParcelable("user", customer)
                }
                UserType.ShopOwner -> {
                    val shopOwner = arguments?.getParcelable("user") ?: ShopOwner()
                    shopOwner.email = emailText
                    bundle.putParcelable("user", shopOwner)
                }
            }

            // Navigate to the PasswordCreationFragment
            val passwordFragment = PasswordCreationFragment()
            passwordFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,  // Animation when entering
                    R.anim.fade_out,  // Animation when exiting
                    R.anim.fade_in,   // Animation when entering from back
                    R.anim.slide_out  // Animation when exiting to back
                )
                .replace(R.id.fragment_container, passwordFragment)
                .addToBackStack("PhoneNumberFragment") // Allow going back to this fragment
                .commit()
        }
    }
}

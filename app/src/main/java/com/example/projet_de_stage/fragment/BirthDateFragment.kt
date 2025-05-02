package com.example.projet_de_stage.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.data.UserType

/**
 * Fragment for selecting and displaying the user's birthdate.
 */
class BirthDateFragment : Fragment() {
    private lateinit var nextButton: Button
    private lateinit var userType: UserType
    private lateinit var userTypeString: String
    private lateinit var birthDateEditText: EditText

    /**
     * Called when the fragment's view is created.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.birth_date, container, false)

        // Initialize UI elements
        birthDateEditText = view.findViewById(R.id.birthdateDisplay)

        // Retrieve user type from the arguments
        userTypeString = arguments?.getString("userType").toString()
        userType = UserType.valueOf(userTypeString)

        nextButton = view.findViewById(R.id.nextButton)
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
            val birthDateText = birthDateEditText.text.toString()

            // Handle user type and update corresponding user object with the selected birthdate
            when (userType) {
                UserType.Barber -> {
                    val barber = arguments?.getParcelable("user") ?: Barber()
                    barber.birthDate = birthDateText
                    bundle.putParcelable("user", barber)
                    Toast.makeText(requireContext(), "Barber: $barber", Toast.LENGTH_SHORT).show()
                }
                UserType.Customer -> {
                    val customer = arguments?.getParcelable("user") ?: Customer()
                    customer.birthDate = birthDateText
                    bundle.putParcelable("user", customer)
                    Toast.makeText(requireContext(), "Customer: $customer", Toast.LENGTH_SHORT).show()
                }
                UserType.ShopOwner -> {
                    val shopOwner = arguments?.getParcelable("user") ?: ShopOwner()
                    shopOwner.birthDate = birthDateText
                    bundle.putParcelable("user", shopOwner)
                    Toast.makeText(requireContext(), "ShopOwner: $shopOwner", Toast.LENGTH_SHORT).show()
                }
            }

            // Pass the user type to the next fragment
            bundle.putString("userType", userType.name)

            // Navigate to the next fragment (PhoneNumberFragment)
            val phoneNumberFragment = EmailFragment()
            phoneNumberFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,  // Slide in animation
                    R.anim.fade_out,  // Fade out animation
                    R.anim.fade_in,   // Fade in animation when returning
                    R.anim.slide_out  // Slide out animation when returning
                )
                .replace(R.id.fragment_container, phoneNumberFragment)
                .addToBackStack("BirthDateFragment")
                .commit()
        }
    }
}

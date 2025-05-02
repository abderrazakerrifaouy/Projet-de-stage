package com.example.projet_de_stage.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.data.UserType

/**
 * Fragment that allows the user to select the type of account they want to create (Barber, Customer, or ShopOwner).
 */
class WelcomeFragment : Fragment() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var confirmButton: Button

    /**
     * Called when the fragment's view is created.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_type, container, false)

        // Initialize UI elements
        radioGroup = view.findViewById(R.id.radioGroup)
        confirmButton = view.findViewById(R.id.nextButton)

        return view
    }

    /**
     * Called after the fragment's view is created and initialized.
     * Handles the logic for the "Confirm" button click.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmButton.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId

            // Check if a selection has been made
            if (selectedId == -1) {
                Toast.makeText(context, "الرجاء اختيار نوع حسابك للمتابعة", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Get the selected radio button
            val radioButton = view.findViewById<RadioButton>(selectedId)

            // Prepare a bundle to pass the data to the next fragment
            val bundle = Bundle()
            val nameFragment = NameInputFragment() // Proceed to NameInputFragment

            when(selectedId) {
                // Barber selected
                R.id.radioBarber -> {
                    bundle.putParcelable("user", Barber()) // Create Barber object
                    bundle.putString("userType", UserType.Barber.name)
                }
                // ShopOwner selected
                R.id.radioAdmin -> {
                    bundle.putParcelable("user", ShopOwner()) // Create ShopOwner object
                    bundle.putString("userType", UserType.ShopOwner.name)
                }
                // Customer selected
                R.id.radioCustomer -> {
                    bundle.putParcelable("user", Customer()) // Create Customer object
                    bundle.putString("userType", UserType.Customer.name)
                }
            }

            // Pass the data to the NameInputFragment
            nameFragment.arguments = bundle

            // Navigate to the NameInputFragment
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,  // Animation for entering
                    R.anim.fade_out,  // Animation for exiting
                    R.anim.fade_in,   // Animation when coming back
                    R.anim.slide_out  // Animation when going back
                )
                .replace(R.id.fragment_container, nameFragment)
                .addToBackStack("welcome_fragment") // Allow going back using the back button
                .commit()
        }
    }
}

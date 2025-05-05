package com.example.projet_de_stage.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.data.UserType
import com.example.projet_de_stage.repository.AuthRepository
import com.example.projet_de_stage.view.LoginActivity
import com.example.projet_de_stage.viewModel.AuthViewModel
import com.google.android.material.textfield.TextInputEditText

/**
 * Fragment for creating and setting a password for the user during registration.
 */
class PasswordCreationFragment : Fragment() {
    private lateinit var nextButton: Button
    private lateinit var passwordInput: TextInputEditText
    private lateinit var userType: UserType
    private lateinit var userTypeString: String
    private var authViewModel: AuthViewModel = AuthViewModel(AuthRepository())

    /**
     * Called when the fragment's view is created.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.password, container, false)

        // Retrieve user type from arguments and initialize UI elements
        userTypeString = arguments?.getString("userType").toString()
        userType = UserType.valueOf(userTypeString)
        passwordInput = view.findViewById(R.id.passwordInput)
        nextButton = view.findViewById(R.id.nextButton)

        return view
    }

    /**
     * Called after the fragment's view is created and initialized.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up the "Next" button click listener
        nextButton.setOnClickListener {
            // Retrieve the password entered by the user
            val passwordText = passwordInput.text.toString()

            // Handle user registration based on user type
            when (userType) {
                UserType.Barber -> {
                    Toast.makeText(requireContext(), "المرجو إكمال بياناتك في الملف الشخصي بعد تسجيل دخولك", Toast.LENGTH_SHORT).show()
                    val barber = arguments?.getParcelable("user") ?: Barber()
                    barber.password = passwordText
                    authViewModel.registerUser(barber) // Register Barber
                }
                UserType.Customer -> {
                    Toast.makeText(requireContext(), "المرجو إكمال بياناتك في الملف الشخصي بعد تسجيل دخولك", Toast.LENGTH_SHORT).show()
                    val customer = arguments?.getParcelable("user") ?: Customer()
                    customer.password = passwordText
                    authViewModel.registerUser(customer) // Register Customer
                }
                UserType.ShopOwner -> {

                    val shopOwner = arguments?.getParcelable("user") ?: ShopOwner()
                    shopOwner.password = passwordText
                    authViewModel.registerUser(shopOwner) // Register ShopOwner
                }
            }

            // After registration, navigate to LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Finish the current activity to prevent returning to this fragment
        }
    }
}

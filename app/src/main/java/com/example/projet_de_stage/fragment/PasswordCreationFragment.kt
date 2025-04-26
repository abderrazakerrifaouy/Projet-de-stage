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
import com.example.projet_de_stage.view.admin.AdminActivityHome
import com.example.projet_de_stage.view.barberUser.BarberActivityHome
import com.example.projet_de_stage.viewModel.AuthViewModel
import com.google.android.material.textfield.TextInputEditText

class PasswordCreationFragment : Fragment() {
    private  lateinit var nextBtn : Button
    private lateinit var password : TextInputEditText
    private lateinit var userType: UserType
    private lateinit var userTypeString: String
    private var authViewModel: AuthViewModel = AuthViewModel(AuthRepository())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  view = inflater.inflate(R.layout.password, container, false)
        userTypeString = arguments?.getString("userType").toString()
        userType = UserType.valueOf(userTypeString)
        password = view.findViewById(R.id.passwordInput)
        nextBtn = view.findViewById(R.id.nextButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle  = Bundle()


        nextBtn.setOnClickListener {
            val passwordText = password.text.toString()
            when (userType) {
                UserType.Barber -> {
                    val barber = arguments?.getParcelable("user")?: Barber()
                    barber.password = passwordText
                    authViewModel.registerUser(barber)



                }
                UserType.Customer -> {
                    val customer = arguments?.getParcelable("user")?: Customer()
                    customer.password = passwordText
                    authViewModel.registerUser(customer)



                }
                UserType.ShopOwner -> {
                    val shopOwner = arguments?.getParcelable("user")?: ShopOwner()
                    shopOwner.password = passwordText
                    authViewModel.registerUser(shopOwner)



                }
            }

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        }
    }
}
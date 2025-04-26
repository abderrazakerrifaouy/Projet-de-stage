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

class PhoneNumberFragment : Fragment() {
    private lateinit var nextBtn : Button
    private lateinit var Email : TextInputEditText
    private lateinit var userTypeString: String
    private lateinit var userType: UserType



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.phone_number, container, false)
        nextBtn = view.findViewById(R.id.nextButton)
        Email = view.findViewById(R.id.phoneInput)
        userTypeString = arguments?.getString("userType").toString()
        userType = UserType.valueOf(userTypeString)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        nextBtn.setOnClickListener {
            val bundle = Bundle()
            val emailText = Email.text.toString()
            bundle.putString("userType",userType.name)

            when (userType) {
                UserType.Barber -> {
                    val barber = arguments?.getParcelable("user")?: Barber()
                    barber.email = emailText
                    bundle.putParcelable("user", barber)
                }
                UserType.Customer -> {
                    val customer = arguments?.getParcelable("user")?: Customer()
                    customer.email = emailText
                    bundle.putParcelable("user",customer)
                }

                UserType.ShopOwner -> {
                    val shopOwner = arguments?.getParcelable("user")?: ShopOwner()
                    shopOwner.email = emailText
                    bundle.putParcelable("user",shopOwner)
                }
            }


            val passwordFragment = PasswordCreationFragment()
            passwordFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,  // حركة الدخول
                    R.anim.fade_out,  // حركة الخروج
                    R.anim.fade_in,   // حركة الدخول عند الرجوع
                    R.anim.slide_out  // حركة الخروج عند الرجوع
                )
                .replace(R.id.fragment_container, passwordFragment)
                .addToBackStack("PhoneNumberFragment")
                .commit()
        }


    }
}
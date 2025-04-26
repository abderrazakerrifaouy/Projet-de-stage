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

class NameInputFragment  : Fragment() {
    private lateinit var nextBtn : Button
    private lateinit var firstName : TextInputEditText
    private lateinit var lastName : TextInputEditText
    private lateinit var userTypeStringe : String
    private lateinit var userType : UserType
    private lateinit var fullName : String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.full_name, container, false)
        nextBtn = view.findViewById(R.id.nextButton)
        firstName = view.findViewById(R.id.firstNameInput)
        lastName = view.findViewById(R.id.lastNameInput)

        userTypeStringe = arguments?.getString("userType").toString()
        userType = UserType.valueOf(userTypeStringe)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = Bundle()




        nextBtn.setOnClickListener {
            fullName = firstName.text.toString() + " " + lastName.text.toString()
            when (userType) {
                UserType.Barber -> {
                    val barber = arguments?.getParcelable("user")?: Barber()
                    barber.name = fullName
                    bundle.putParcelable("user", barber)
                }
                UserType.Customer -> {
                    val customer = arguments?.getParcelable("user")?: Customer()
                    customer.name = fullName
                    bundle.putParcelable("user",customer)
                }
                UserType.ShopOwner -> {
                    val shopOwner = arguments?.getParcelable("user")?: ShopOwner()
                    shopOwner.name = fullName
                    bundle.putParcelable("user",shopOwner)
                }
            }
            bundle.putString("userType",userTypeStringe)



            val birthDateFragment = BirthDateFragment()
            birthDateFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,  // حركة الدخول
                    R.anim.fade_out,  // حركة الخروج
                    R.anim.fade_in,   // حركة الدخول عند الرجوع
                    R.anim.slide_out  // حركة الخروج عند الرجوع
                )
                .replace(R.id.fragment_container, birthDateFragment)
                .addToBackStack("NameInputFragment") // لإمكانية العودة بالزر الخلفي
                .commit()
        }



    }
}
package com.example.projet_de_stage.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R

class PhoneNumberFragment : Fragment() {
    private lateinit var nextBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.phone_number, container, false)
        nextBtn = view.findViewById(R.id.nextButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        nextBtn.setOnClickListener {
            val passwordFragment = PasswordCreationFragment()

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
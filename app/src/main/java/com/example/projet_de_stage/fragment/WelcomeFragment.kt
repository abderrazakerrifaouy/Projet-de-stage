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


class WelcomeFragment : Fragment() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var btnConfirm: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // تحميل واجهة Fragment
        val view = inflater.inflate(R.layout.fragment_user_type, container, false)

        // تهيئة العناصر
        radioGroup = view.findViewById(R.id.radioGroup)
        btnConfirm = view.findViewById(R.id.nextButton)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnConfirm.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId

            if (selectedId == -1) {
                Toast.makeText(context, "الرجاء اختيار نوع حسابك للمتابعة", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val radioButton = view.findViewById<RadioButton>(selectedId)

            // الانتقال إلى Fragment جديد
            val nameFragment = NameInputFragment() // يجب أن يكون Fragment وليس Activity

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,  // حركة الدخول
                    R.anim.fade_out,  // حركة الخروج
                    R.anim.fade_in,   // حركة الدخول عند الرجوع
                    R.anim.slide_out  // حركة الخروج عند الرجوع
                )
                .replace(R.id.fragment_container, nameFragment)
                .addToBackStack("welcome_fragment") // لإمكانية العودة بالزر الخلفي
                .commit()
        }
    }
}
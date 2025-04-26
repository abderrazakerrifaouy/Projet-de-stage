package com.example.projet_de_stage.fragment.fragmentBarber

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.view.LoginActivity

class ProfileFragment : Fragment() {
    private lateinit var barber: Barber
    private lateinit var profileImage: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvExperience: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvAddress: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnLogAut: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize views
        profileImage = view.findViewById(R.id.profileImage)
        tvName = view.findViewById(R.id.tvName)
        tvExperience = view.findViewById(R.id.tvExperience)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvPhone = view.findViewById(R.id.tvPhone)
        tvAddress = view.findViewById(R.id.tvAddress)
        btnEditProfile = view.findViewById(R.id.btnEditProfile)
        btnLogAut = view.findViewById(R.id.btnLogAut)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get barber data from arguments
//        arguments?.getParcelable<Barber>("barber")?.let {
//            barber = it
//            bindBarberData()
//        }
        btnLogAut.setOnClickListener {
            // مسح الـ UID من SharedPreferences ثم الانتقال لشاشة تسجيل الدخول
            val prefs = requireContext().getSharedPreferences("prefs", MODE_PRIVATE)
            prefs.edit()
                .putString("uid", "")
                .apply()

            // التنقل إلى LoginActivity وإنهاء الـ Activity الحالي
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        // For testing, create a dummy barber
        barber = Barber(
            uid = "1",
            name = "أحمد محمد",
            experience = "0",
            email = "john.c.breckinridge@altostrat ",
            phone = "+966501234567",
            password = "password123",
            shopId = "shop123",
            imageRes = R.drawable.my_profile,
            rating = 2.5f
        )
        bindBarberData()

        btnEditProfile.setOnClickListener {
            // Handle edit profile button click
        }
    }

    private fun bindBarberData() {
        profileImage.setImageResource(barber.imageRes)
        tvName.text = barber.name
        tvExperience.text = "خبرة: ${barber.experience} سنوات"
        tvEmail.text = barber.email
        tvPhone.text = barber.phone
    }

    companion object {
        fun newInstance(barber: Barber): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("barber", barber)
                }
            }
        }
    }
}
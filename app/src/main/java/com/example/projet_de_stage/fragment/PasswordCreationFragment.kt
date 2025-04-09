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
import com.example.projet_de_stage.view.admin.AdminActivityHome

class PasswordCreationFragment : Fragment() {
    private  lateinit var nextBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  view = inflater.inflate(R.layout.password, container, false)
        nextBtn = view.findViewById(R.id.nextButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nextBtn.setOnClickListener {
            val intent = Intent(requireContext(), AdminActivityHome::class.java)
            startActivity(intent)
        }
    }
}
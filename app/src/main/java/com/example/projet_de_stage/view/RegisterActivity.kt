package com.example.projet_de_stage.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_de_stage.R
import com.example.projet_de_stage.fragment.WelcomeFragment

/**
 * This activity is used to handle the registration process.
 * It displays the WelcomeFragment initially.
 */
class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Display the WelcomeFragment when the activity is created
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, WelcomeFragment()) // Replaces the container with WelcomeFragment
            .commit()
    }
}

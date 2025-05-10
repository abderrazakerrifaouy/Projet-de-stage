package com.example.projet_de_stage.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projet_de_stage.R
import com.example.projet_de_stage.fragment.WelcomeFragment
import java.util.Locale


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // استرجاع اللغة المخزنة من SharedPreferences
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val langCode = prefs.getString("selected_language", Locale.getDefault().language)
        changeAppLanguage(langCode ?: Locale.getDefault().language)

        // عرض WelcomeFragment عند إنشاء النشاط
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, WelcomeFragment())
            .commit()
    }


    private fun changeAppLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}

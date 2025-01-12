package com.example.wallpaper_dead_reviewed.api.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wallpaper_dead_reviewed.R
import com.google.firebase.auth.FirebaseAuth

class UserRegistrationActivity : AppCompatActivity() {

    private lateinit var edittextEmail: AppCompatEditText
    private lateinit var edittextUsername: AppCompatEditText
    private lateinit var editTextPassword: AppCompatEditText
    private lateinit var btn_register : ImageButton
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_WallpaperApp) // Set your custom theme here
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_user_registration)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_register = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progress_bar)

        edittextEmail = findViewById(R.id.et_register_email)
        if (edittextEmail == null) {
            Log.e("Error", "et_register_email not found")
            Toast.makeText(this, "Error: Email not found", Toast.LENGTH_SHORT).show()
            return
        }

        edittextUsername = findViewById(R.id.et_register_username)
        if (edittextUsername == null) {
            Log.e("Error", "et_register_username not found")
            Toast.makeText(this, "Error: username not found", Toast.LENGTH_SHORT).show()
            return
        }

        editTextPassword = findViewById(R.id.et_register_password)
        if (editTextPassword == null) {
            Log.e("Error", "et_register_password not found")
            Toast.makeText(this, "Error: password not found", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the user has registered
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isRegistered = sharedPreferences.getBoolean("is_registered", false)


        btn_register.setOnClickListener(View.OnClickListener {

            val email = edittextEmail.text.toString()
            val userName = edittextUsername.text.toString()
            val password = editTextPassword.text.toString()

            if(email.isEmpty() || userName.isEmpty() || password.isEmpty() ){
                Toast.makeText(this , "Please fill All details" , Toast.LENGTH_SHORT).show()
            }
            if (::progressBar.isInitialized) {
                progressBar.visibility = View.VISIBLE
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration successful, store the flag in SharedPreferences
                        sharedPreferences.edit().putBoolean("You're registered", true).apply()
                        // Hide the progress bar
                        progressBar.visibility = View.GONE
                        // Redirect the user to the next activity
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Registration failed, display an error message
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        Log.e("RegistrationError", "Error: ${task.exception?.message}")
                        // Hide the progress bar
                        progressBar.visibility = View.GONE
                    }
                }
        })

    }
}
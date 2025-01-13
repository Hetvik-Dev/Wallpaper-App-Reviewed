package com.example.wallpaper_dead_reviewed.api.presentation.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wallpaper_dead_reviewed.R
import com.example.wallpaperapp.presentation.fragments.UserProfileScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: ImageButton
    private lateinit var btnGoToRegister: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var btnForgotPassword : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_WallpaperApp) // Set your custom theme here
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        //if User is already Logged in then
        @Override fun onStart(){
            super.onStart()
            val currentUser :  FirebaseUser? = auth.currentUser
            if(currentUser != null){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Initialize views
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        btnGoToRegister = findViewById(R.id.btn_go_to_register)
        progressBar = ProgressBar(this)  // Create a ProgressBar dynamically if not in XML
        btnForgotPassword = findViewById(R.id.btn_forgot_password)

        // Set login button click listener
        btnLogin.setOnClickListener {
            loginUser()
        }

        // Set button to go to registration activity
        btnGoToRegister.setOnClickListener {
            val intent = Intent(this, UserRegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set forgot password button click listener
        btnForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun loginUser () {
        val email = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        val userName = intent.getStringExtra("USER_NAME") ?: "Unknown"

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                    val userProfileFragment = UserProfileScreen().apply {
                        arguments = Bundle().apply {
                            Log.d("LoginActivity", "User Name: $userName, User Email: $email")
                            putString("USER_NAME", userName)
                            putString("USER_EMAIL", email)
                        }
                    }

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_login, userProfileFragment)
                        .addToBackStack(null)
                        .commit()

//                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
//                    }, 500)

                } else {
                    Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    showRegistrationPrompt()
                }
            }
    }

    private fun showRegistrationPrompt() {
        AlertDialog.Builder(this)
            .setTitle("User  Not Found")
            .setMessage("It seems you are not registered. Would you like to register?")
            .setPositiveButton("Yes") { _, _ ->
                // Navigate to UserRegistrationActivity
                val intent = Intent(this, UserRegistrationActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showForgotPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reset Password")

        val input = EditText(this)
        input.hint = "Enter your email"
        input.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        builder.setView(input)

        builder.setPositiveButton("Send") { _, _ ->
            val email = input.text.toString().trim()
            if (email.isNotEmpty()) {
                sendPasswordResetEmail(email)
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent to $email", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to send reset email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

package com.example.wallpaper_dead_reviewed.api.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wallpaper_dead_reviewed.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class UserRegistrationActivity : AppCompatActivity() {

    private lateinit var edittextEmail: AppCompatEditText
    private lateinit var edittextUsername: AppCompatEditText
    private lateinit var editTextPassword: AppCompatEditText
    private lateinit var btn_register: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var btn_goto_login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_WallpaperApp) // Set your custom theme here
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        Log.d("User  RegistrationActivity", "Setting content view")
        setContentView(R.layout.activity_user_registration)
        Log.d("User  RegistrationActivity", "Finding views")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_register = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progress_bar)
        btn_goto_login = findViewById(R.id.btn_go_to_login)

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



        btn_register.setOnClickListener(View.OnClickListener {

            val email = edittextEmail.text.toString().trim()
            val userName = edittextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isEmpty() || userName.isEmpty() || password.isEmpty()) {
                Snackbar.make(it, "Please fill all details", Snackbar.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (!isValidEmail(email)) {
                Snackbar.make(it, "Please enter a valid email", Snackbar.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (!isValidPassword(password)) {
                Snackbar.make(it, "Password must be at least 6 characters", Snackbar.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }

            if (::progressBar.isInitialized) {
                progressBar.visibility = View.VISIBLE
            }

            checkUserExists(email) { userExists ->
                if (userExists) {

                    showEmailAlreadyRegisteredDialog()
                    progressBar.visibility = View.GONE
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                sharedPreferences.edit().putBoolean("is_registered", true).apply()

                                progressBar.visibility = View.GONE

                                edittextEmail.text?.clear()
                                edittextUsername.text?.clear()
                                editTextPassword.text?.clear()
                                // Redirect to LoginActivity

                                val intent = Intent(this, LoginActivity::class.java).apply {
                                    putExtra("USER_NAME", userName)
                                }
                                startActivity(intent)
                                finish()
                            } else {

                                Snackbar.make(
                                    it,
                                    "Registration failed: ${task.exception?.message}",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                Log.e("RegistrationError", "Error: ${task.exception?.message}")

                                progressBar.visibility = View.GONE
                            }
                        }
                }
            }
        })

        btn_goto_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun checkUserExists(email: String, onResult: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    onResult(signInMethods != null && signInMethods.isNotEmpty())
                } else {
                    Log.e(
                        "CheckUser Exists",
                        "Error checking user existence: ${task.exception?.message}"
                    )
                    onResult(false) // Assume user does not exist on error
                }
            }
    }

    private fun showEmailAlreadyRegisteredDialog() {
        AlertDialog.Builder(this)
            .setTitle("Email Already Registered")
            .setMessage("A user is already registered with this email address. Please use a different email.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6 // You can add more complex rules here
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
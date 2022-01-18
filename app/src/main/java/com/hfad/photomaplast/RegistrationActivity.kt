package com.hfad.photomaplast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        databaseReference = database?.reference?.child("profile")
        database = FirebaseDatabase.getInstance()

        register()

    }

    private fun register() {
        val registerButton = findViewById<Button>(R.id.registerButton)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)

        registerButton.setOnClickListener {

            when {
                TextUtils.isEmpty(emailInput.text.toString()) -> {
                    emailInput.error = "Please enter email"
                    return@setOnClickListener
                }
                TextUtils.isEmpty(passwordInput.text.toString()) -> {
                    passwordInput.error = "Please enter password"
                    return@setOnClickListener
                }
                TextUtils.isEmpty(nameInput.text.toString()) -> {
                    nameInput.error = "Please enter your name"
                    return@setOnClickListener
                }
                else -> auth.createUserWithEmailAndPassword(emailInput.text.toString(),
                    passwordInput.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val currentUser = auth.currentUser
                            val currentUserDb = databaseReference?.child((currentUser?.uid!!))

                            Toast.makeText(this@RegistrationActivity,
                                "Registration Success!",
                                Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(this@RegistrationActivity,
                                "Registration failed, please try again!",
                                Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

    }
}
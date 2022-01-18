package com.hfad.photomaplast

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val rcSingIn = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_WEB_APPLICATION_CLIENT_ID")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
        login()
    }
    private fun login() {
        val loginButton = findViewById<Button>(R.id.loginButton)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val registerButton = findViewById<TextView>(R.id.registerButton)
        val logoImage = findViewById<ImageView>(R.id.logoImage)
        val googleButton = findViewById<Button>(R.id.googleButton)

        loginButton.setOnClickListener {

            if(TextUtils.isEmpty(emailInput.text.toString())) {
                emailInput.error = "Please enter email"
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(passwordInput.text.toString())) {
                passwordInput.error = "Please enter password"
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(emailInput.text.toString(),passwordInput.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed, please try again!", Toast.LENGTH_LONG).show()

                    }
                }
        }

        registerButton.setOnClickListener{
            startActivity(Intent(this@LoginActivity,RegistrationActivity::class.java))
        }



        googleButton.setOnClickListener {
            signIn()
        }


    }
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, rcSingIn
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == rcSingIn) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            // Signed in successfully
            val googleId = account?.id ?: ""
            Log.i("Google ID",googleId)

            val googleFirstName = account?.givenName ?: ""
            Log.i("Google First Name", googleFirstName)

            val googleLastName = account?.familyName ?: ""
            Log.i("Google Last Name", googleLastName)

            val googleEmail = account?.email ?: ""
            Log.i("Google Email", googleEmail)

            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)

            val googleIdToken = account?.idToken ?: ""
            Log.i("Google ID Token", googleIdToken)

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e(
                "failed code=", e.statusCode.toString()
            )
        }
    }
    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                // Update your UI here
            }
    }
    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(this) {
                // Update your UI here
            }
    }
}
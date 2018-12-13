package com.example.marty.feed_me

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginClick(v : View) {
        if (!isFormValid())
            return

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                etEmail.text.toString(), etPassword.text.toString()
        ).addOnSuccessListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }.addOnFailureListener {
            Toast.makeText(this@LoginActivity, "Login Error: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun registerClick(v: View) {
        if (!isFormValid())
            return

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                etEmail.text.toString(), etPassword.text.toString()
        ).addOnSuccessListener {
            val user = it.user
            user.updateProfile(
                    UserProfileChangeRequest.Builder().
                            setDisplayName(userNameFromEmail(user.email!!)).build()
            )
            Toast.makeText(this@LoginActivity, getString(R.string.registration_ok), Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this@LoginActivity, "Register Error: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun isFormValid(): Boolean {
        return when {
            etEmail.text.isEmpty() -> {
                etEmail.error = getString(R.string.empty_field)
                false
            }
            etPassword.text.isEmpty() -> {
                etPassword.error = getString(R.string.empty_field)
                false
            }
            else -> true
        }
    }

    private fun userNameFromEmail(email: String) = email.substringBefore("@")
}
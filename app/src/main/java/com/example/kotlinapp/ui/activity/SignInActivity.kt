package com.example.kotlinapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinapp.MainActivity
import com.example.kotlinapp.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth


class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Redirecting to Sign Up", Toast.LENGTH_SHORT).show()
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                Toast.makeText(this, "Signing in...", Toast.LENGTH_SHORT).show()

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SplashActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    code redirect at sign in page
//    override fun onStart() {
//        super.onStart()
//
//        if(firebaseAuth.currentUser != null){
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
//    }

}


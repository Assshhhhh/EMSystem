package com.example.cashtrack.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cashtrack.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.tvMoveToSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // Shared Prefs
        mAuth.addAuthStateListener {
            if (mAuth.currentUser != null){
                try {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                } catch (e:Exception){

                }
            }
        }

        binding.buttonLogin.setOnClickListener {
            var email:String = binding.editLoginEmail.text.toString().trim()
            var password:String = binding.editLoginPassword.text.toString().trim()

            if (email.equals("")){
                Toast.makeText(this,"Please Enter Your Email", Toast.LENGTH_SHORT).show()
                binding.editLoginEmail.error = "Email is Required!"
            }
            else if (password.equals("")){
                Toast.makeText(this,"Please Enter Your Password", Toast.LENGTH_SHORT).show()
                binding.editLoginEmail.error = "Password is Required!"
            }
            else {

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        binding.editLoginEmail.setText("")
                        binding.editLoginPassword.setText("")
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

                }

            }
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Reset Password", Toast.LENGTH_SHORT).show()
        }

    }
}
package com.example.cashtrack.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cashtrack.ModelClass.AuthModel
import com.example.cashtrack.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.tvMoveToLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.buttonSignup.setOnClickListener {
            var name:String = binding.editName.text.toString().trim()
            var email:String = binding.editEmail.text.toString().trim()
            var password:String = binding.editPassword.text.toString().trim()

            if (name.equals("")){
                Toast.makeText(this,"Please Enter Your Name", Toast.LENGTH_SHORT).show()
                binding.editName.error = "Name is Required!"
            }
            else if (email.equals("")){
                Toast.makeText(this,"Please Enter Your Email", Toast.LENGTH_SHORT).show()
                binding.editEmail.error = "Email is Required!"
            }
            else if (password.equals("")){
                Toast.makeText(this,"Please Enter Your Password", Toast.LENGTH_SHORT).show()
                binding.editEmail.error = "Password is Required!"
            }
            else {

                var authData: AuthModel = AuthModel(name, email, password)

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Account Signed Up", Toast.LENGTH_SHORT).show()
                        binding.editName.setText("")
                        binding.editEmail.setText("")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Failed! Try again", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

                }
            }
        }

    }
}
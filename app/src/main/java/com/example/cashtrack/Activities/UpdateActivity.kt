package com.example.cashtrack.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import com.example.cashtrack.databinding.ActivityUpdateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var newType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseUser = mAuth.currentUser!!

        //var id:String = intent.getStringExtra("id").toString()
        var note:String = intent.getStringExtra("note").toString()
        var amount:String = intent.getStringExtra("amount").toString()
        var type:String = intent.getStringExtra("type").toString()

        binding.userNoteAdd.setText(note)
        binding.userAmountAdd.setText(amount)

        when (type){
            "income" -> {
                newType = "income"
                binding.incomeCheckBox.isChecked = true
            }
            "expense" -> {
                newType = "expense"
                binding.expenseCheckBox.isChecked = true
            }
            else -> {
                Toast.makeText(this, "Failed to retrieve transaction type!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.expenseCheckBox.setOnClickListener {
            newType = "expense"
            binding.expenseCheckBox.isChecked = true
            binding.incomeCheckBox.isChecked = false
        }

        binding.incomeCheckBox.setOnClickListener {
            newType = "income"
            binding.expenseCheckBox.isChecked = false
            binding.incomeCheckBox.isChecked = true
        }

        binding.btnAddUpdate.setOnClickListener {

            val id:String = intent.getStringExtra("id").toString()
            //Toast.makeText(this, id, Toast.LENGTH_LONG).show()    //No problem here.. Getting the same id
            val updatedNote = binding.userNoteAdd.text.toString().trim()
            val updatedAmount = binding.userAmountAdd.text.toString().trim()

            if (updatedAmount.equals("")){
                Toast.makeText(this, "Please enter an amount!", Toast.LENGTH_SHORT).show()
            }
            else if (newType.equals("")){
                Toast.makeText(this, "Please select transaction type!", Toast.LENGTH_SHORT).show()
            }
            else {
                firebaseFirestore.collection("Expenses").document(firebaseUser.uid)
                    .collection("Transactions").document(id)
                    .update(
                        "note", updatedNote,
                        "amount", updatedAmount,
                        "type", newType
                    ).addOnSuccessListener {
                        Toast.makeText(this, "Transaction Updated", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
            }
        }

        binding.btnAddDelete.setOnClickListener {

            val id:String = intent.getStringExtra("id").toString()

            firebaseFirestore.collection("Expenses").document(firebaseUser.uid)
                .collection("Transactions").document(id)
                .delete().addOnSuccessListener {
                    Toast.makeText(this, "Transaction Deleted", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

        }

    }
}
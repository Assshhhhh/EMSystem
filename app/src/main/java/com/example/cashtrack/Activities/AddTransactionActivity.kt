package com.example.cashtrack.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cashtrack.ModelClass.TransactionModel
import com.example.cashtrack.databinding.ActivityAddTransactionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestoreRef: FirebaseFirestore
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var currentTime: String

    private lateinit var type: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        firestoreRef = FirebaseFirestore.getInstance()
        firebaseUser = mAuth.currentUser!!

        val sdf: SimpleDateFormat = SimpleDateFormat("dd MMMM, yyyy HH:mm a", Locale.getDefault())
        currentTime = sdf.format(Date())

        binding.expenseCheckBox.setOnClickListener {
            type = "expense"
            binding.expenseCheckBox.isChecked = true
            binding.incomeCheckBox.isChecked = false
        }
        binding.incomeCheckBox.setOnClickListener {
            type = "income"
            binding.expenseCheckBox.isChecked = false
            binding.incomeCheckBox.isChecked = true
        }

        binding.btnAddTransaction.setOnClickListener {

            var id: String = UUID.randomUUID().toString()

            var note: String = binding.userNoteAdd.text.toString().trim()
            var amount: String = binding.userAmountAdd.text.toString().trim()
            var date: String = currentTime
            var transType: String = type

            if (amount.equals("")){
                Toast.makeText(this, "Please enter an amount!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (transType.equals("")){
                Toast.makeText(this, "Please select transaction type!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                var transaction: TransactionModel = TransactionModel(note, amount, date, type, id)

                firestoreRef.collection("Expenses").document(firebaseUser.uid)
                    .collection("Transactions").document(id).set(transaction)
                    .addOnCompleteListener {

                        if (it.isSuccessful) {

                            binding.userNoteAdd.setText("")
                            binding.userAmountAdd.setText("")
                            binding.expenseCheckBox.isChecked = false
                            binding.incomeCheckBox.isChecked = false
                            try {
                                startActivity(Intent(this, DashboardActivity::class.java))
                                finishAffinity()
                            } catch (e: Exception) {

                            }

                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to Add Transaction", Toast.LENGTH_SHORT).show()
                    }
            }
        }

    }
}
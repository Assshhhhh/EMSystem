package com.example.cashtrack.Activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cashtrack.AdapterClass.TransactionAdapter
import com.example.cashtrack.ModelClass.TransactionModel
import com.example.cashtrack.R
import com.example.cashtrack.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestoreRef: FirebaseFirestore
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var transList: ArrayList<TransactionModel>
    private lateinit var transAdapter: TransactionAdapter
    private var incomeSum:Int = 0
    private var expenseSum:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Firebase Firestore Section
        mAuth = FirebaseAuth.getInstance()
        firestoreRef = FirebaseFirestore.getInstance()
        firebaseUser = mAuth.currentUser!!

        //Shared Prefs
        mAuth.addAuthStateListener {
            if (firebaseUser==null){
                try {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                } catch (e:Exception){

                }
            }
        }

        //Recycler Section
        transList = ArrayList<TransactionModel>()

        val linearLayoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.layoutManager = linearLayoutManager
        binding.historyRecyclerView.setHasFixedSize(true)

        //Fetch from Firestore on Recycler
        fetchFirestoreData()

        //Refresh
        binding.refreshBtn.setOnClickListener {
            try {
                startActivity(Intent(this, DashboardActivity::class.java))
                finishAffinity()
            } catch(e:Exception){

            }
        }

        //Logout
        binding.signOutBtn.setOnClickListener {

            showLogoutDialog()

        }

        //Floating Button
        binding.addFloatingBtn.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }



    }

    private fun fetchFirestoreData() {
        firestoreRef.collection("Expenses").document(mAuth.uid!!)
            .collection("Transactions").get().addOnCompleteListener {

                transList.clear()
                for (documentSnapshot:DocumentSnapshot in it.result){
                    var model:TransactionModel = TransactionModel(

                        documentSnapshot.getString("note") ?: "",
                        documentSnapshot.getString("amount") ?: "",
                        documentSnapshot.getString("date") ?: "",
                        documentSnapshot.getString("type") ?: "",
                        documentSnapshot.getString("id") ?: ""
                    )

                    model.let { transList.add(it)}

                    //Manipulating values
                    var amount:Int = Integer.parseInt(documentSnapshot.getString("amount")!!)
                    if (documentSnapshot.getString("type").equals("expense"))
                    {
                        expenseSum = expenseSum + amount
                    }
                    else{
                        incomeSum = incomeSum + amount
                    }

                    binding.TotalIncome.setText(incomeSum.toString())
                    binding.totalExpense.setText(expenseSum.toString())
                    binding.totalBalance.setText((incomeSum - expenseSum).toString())

                }

                transAdapter = TransactionAdapter(this, transList)
                binding.historyRecyclerView.adapter = transAdapter

            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }

    }

    private fun showLogoutDialog() {
        val builder:AlertDialog = AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                mAuth.signOut()
                dialog.cancel()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })
            .show()
    }
}
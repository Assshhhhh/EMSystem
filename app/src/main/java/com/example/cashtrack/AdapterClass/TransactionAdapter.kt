package com.example.cashtrack.AdapterClass

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cashtrack.Activities.UpdateActivity
import com.example.cashtrack.ModelClass.TransactionModel
import com.example.cashtrack.R
import com.example.cashtrack.databinding.ItemTransactionBinding

class TransactionAdapter(private var context: Context, private var arratList: ArrayList<TransactionModel>)
    :RecyclerView.Adapter<TransactionAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false))
    }

    override fun getItemCount(): Int {
        return arratList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data:TransactionModel = arratList[position]

        holder.binding.noteOne.setText(data.note)
        holder.binding.amountOne.setText(data.amount)
        holder.binding.dateOne.setText(data.date)

        if (data.type.equals("expense")) {
            holder.binding.priorityOne.setBackgroundResource(R.drawable.red_shape)
        } else if (data.type.equals("income")){
            holder.binding.priorityOne.setBackgroundResource(R.drawable.green_shape)
            //holder.binding.amountOne.setTextColor(R.color.green)
        }

        holder.itemView.setOnClickListener {

            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("id", data.id)
            intent.putExtra("note", data.note)
            intent.putExtra("amount", data.amount)
            intent.putExtra("type", data.type)

            context.startActivity(intent)

        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding:ItemTransactionBinding = ItemTransactionBinding.bind(itemView)
    }

}
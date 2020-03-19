package com.example.adobe_exp_batch_status_android.Batch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adobe_exp_batch_status_android.Objects.BatchError
import com.example.adobe_exp_batch_status_android.R
import kotlinx.android.synthetic.main.item_batch_error.view.*

class BatchErrorsRecyclerViewAdapter(var errors: List<BatchError>) : RecyclerView.Adapter<BatchErrorsRecyclerViewAdapter.ViewHolder>()  {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchErrorsRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_batch_error, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: BatchErrorsRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(errors[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return errors.size
    }



    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(error: BatchError) {
            itemView.error_description_label.text = error.errorDetails
            itemView.error_details_label.text = error.errorCode
        }
    }
}
package com.example.adobe_exp_batch_status_android.BatchesList

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adobe_exp_batch_status_android.Objects.Batch
import com.example.adobe_exp_batch_status_android.R
import kotlinx.android.synthetic.main.item_batch_list.view.*

import java.util.*


class BatchesListRecyclerViewAdapter(var datasets: List<Batch>, var callback: BatchesListContract.BatchesListFragmentInterface) : RecyclerView.Adapter<BatchesListRecyclerViewAdapter.ViewHolder>()  {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchesListRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_datasets, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: BatchesListRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(datasets[position], callback)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return datasets.size
    }



    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(batch: Batch, callback: BatchesListContract.BatchesListFragmentInterface) {
            itemView.batch_id_text_view.text = batch.id
            itemView.batch_updated_text_view.text = getUpdatedTime(batch.lastUpdated)
            itemView.batch_status_icon.radius = (itemView.batch_status_icon.width / 2).toFloat()

            when(batch.status) {
                "success" -> {
                    itemView.batch_status_label.text = "Success"
                    itemView.batch_status_icon.setCardBackgroundColor(Color.GREEN)
                }
                "failure", "failed" -> {
                    itemView.batch_status_label.text = "Failure"
                    itemView.batch_status_icon.setCardBackgroundColor(Color.RED)
                }
                "aborted", "abandoned", "inactive" -> {
                    itemView.batch_status_label.text = "Aborted"
                    itemView.batch_status_icon.setCardBackgroundColor(Color.GRAY)
                }
                else -> {
                    itemView.batch_status_label.text = "Processing"
                    itemView.batch_status_icon.setCardBackgroundColor(Color.YELLOW)
                }
            }

            itemView.setOnClickListener {
                callback.batchSelected(batch)
            }
        }

        fun getUpdatedTime(unixTimestamp: Int): String {
            val currentCalendar = Calendar.getInstance()
            val currentTime = currentCalendar.timeInMillis

            val difference = currentTime - unixTimestamp

            val seconds = difference / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            return when {
                days > 0 -> {
                    "Updated $days days ago"
                }
                hours > 0 -> {
                    "Updated $hours hours ago"
                }
                minutes > 0 -> {
                    "Updated $minutes minutes ago"
                }
                else -> {
                    "Update recently"
                }
            }

        }
    }
}
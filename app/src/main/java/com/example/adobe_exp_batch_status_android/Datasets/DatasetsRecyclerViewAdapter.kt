package com.example.adobe_exp_batch_status_android.Datasets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adobe_exp_batch_status_android.Objects.Dataset
import com.example.adobe_exp_batch_status_android.R
import kotlinx.android.synthetic.main.item_datasets.view.*

class DatasetsRecyclerViewAdapter(var datasets: List<Dataset>, var callback: DatasetsContract.DatasetFragmentInterface) : RecyclerView.Adapter<DatasetsRecyclerViewAdapter.ViewHolder>()  {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatasetsRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_datasets, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: DatasetsRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(datasets[position], callback)

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return datasets.size
    }



    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(dataset: Dataset, callback: DatasetsContract.DatasetFragmentInterface) {
            itemView.dataset_name_text_view.text = dataset.name
            itemView.dataset_id_text_view.text = dataset.id
            itemView.dataset_status_text_view.text = dataset.state

            itemView.setOnClickListener {
                callback.datasetSelected(dataset)
            }
        }
    }
}
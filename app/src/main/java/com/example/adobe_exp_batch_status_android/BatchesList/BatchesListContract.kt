package com.example.adobe_exp_batch_status_android.BatchesList

import com.example.adobe_exp_batch_status_android.Objects.Batch

class BatchesListContract {
    interface BatchesListPresenterInterface {
        fun retrieveBatches(dataset: String)
        fun updateBatches(batches: ArrayList<Batch>)
    }

    interface BatchesListFragmentInterface {
        fun updateBatches(batches: ArrayList<Batch>)
        fun batchSelected(batch: Batch)
    }

    interface BatchesListModelInterface {
        fun retrieveBatches(dataset: String)
    }
}
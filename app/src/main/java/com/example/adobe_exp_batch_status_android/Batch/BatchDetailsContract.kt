package com.example.adobe_exp_batch_status_android.Batch

import com.example.adobe_exp_batch_status_android.Objects.BatchDetails

class BatchDetailsContract {
    interface BatchDetailsPresenterInterface {
        fun retrieveBatch(batch: String)
        fun updateBatches(batch: BatchDetails)
    }

    interface BatchDetailsFragmentInterface {
        fun updateBatch(batch: BatchDetails)
    }

    interface BatchDetailsModelInterface {
        fun retrieveBatch(batch: String)
    }
}
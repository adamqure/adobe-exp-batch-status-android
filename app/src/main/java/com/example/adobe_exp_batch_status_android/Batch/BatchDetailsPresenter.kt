package com.example.adobe_exp_batch_status_android.Batch

import com.example.adobe_exp_batch_status_android.Objects.BatchDetails

class BatchDetailsPresenter(var fragmentCallback: BatchDetailsContract.BatchDetailsFragmentInterface) : BatchDetailsContract.BatchDetailsPresenterInterface {

    val model = BatchDetailsModel(this)

    override fun retrieveBatch(batch: String) {
        model.retrieveBatch(batch)
    }

    override fun updateBatches(batch: BatchDetails) {
        fragmentCallback.updateBatch(batch)
    }
}
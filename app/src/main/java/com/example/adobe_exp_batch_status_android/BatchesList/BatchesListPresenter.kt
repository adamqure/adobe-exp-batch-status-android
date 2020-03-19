package com.example.adobe_exp_batch_status_android.BatchesList

import com.example.adobe_exp_batch_status_android.Objects.Batch

class BatchesListPresenter(var fragmentCallback: BatchesListContract.BatchesListFragmentInterface) : BatchesListContract.BatchesListPresenterInterface {

    val model = BatchesListModel(this)

    override fun retrieveBatches(dataset: String) {
        model.retrieveBatches(dataset)
    }

    override fun updateBatches(batches: ArrayList<Batch>) {
        fragmentCallback.updateBatches(batches)
    }


}
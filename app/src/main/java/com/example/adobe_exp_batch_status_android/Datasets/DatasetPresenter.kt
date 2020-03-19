package com.example.adobe_exp_batch_status_android.Datasets

import com.example.adobe_exp_batch_status_android.Objects.Dataset

class DatasetPresenter(var fragmentCallback: DatasetsContract.DatasetFragmentInterface) : DatasetsContract.DatasetPresenterInterface {

    val model = DatasetsModel(this)

    override fun retrieveDatasets() {
        model.retrieveDatasets()
    }

    override fun updateDatasets(datasets: ArrayList<Dataset>) {
        fragmentCallback.updateDatasets(datasets)
    }

}
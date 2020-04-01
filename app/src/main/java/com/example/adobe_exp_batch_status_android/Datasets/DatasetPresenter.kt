package com.example.adobe_exp_batch_status_android.Datasets

import com.example.adobe_exp_batch_status_android.Objects.Dataset

class DatasetPresenter(var fragmentCallback: DatasetsContract.DatasetFragmentInterface) : DatasetsContract.DatasetPresenterInterface {

    lateinit var model: DatasetsContract.DatasetModelInterface

    override fun retrieveDatasets() {
        model = DatasetsModel(this)
        model.retrieveDatasets()
    }

    override fun updateDatasets(datasets: ArrayList<Dataset>) {
        fragmentCallback.updateDatasets(datasets)
    }

}
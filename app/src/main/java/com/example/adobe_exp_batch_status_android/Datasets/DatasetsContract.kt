package com.example.adobe_exp_batch_status_android.Datasets

import com.example.adobe_exp_batch_status_android.Objects.Dataset

class DatasetsContract {
    interface DatasetPresenterInterface {
        fun retrieveDatasets()
        fun updateDatasets(datasets: ArrayList<Dataset>)
    }

    interface DatasetFragmentInterface {
        fun datasetSelected(dataset: Dataset)
        fun updateDatasets(datasets: ArrayList<Dataset>)
    }

    interface DatasetModelInterface {
        fun retrieveDatasets()
    }
}
package com.example.adobe_exp_batch_status_android.Datasets

import com.example.adobe_exp_batch_status_android.API
import com.example.adobe_exp_batch_status_android.Authentication
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response


class DatasetsModel(var presenterCallback: DatasetsContract.DatasetPresenterInterface) : DatasetsContract.DatasetModelInterface {
    override fun retrieveDatasets() {
        val headers = HashMap<String, String>()
        headers.put("Authorization", "Bearer: " + Authentication.authInfo.accessToken)
        headers.put("x-api-key", Authentication.authInfo.apiKey)
        headers.put("x-gw-ims-org-id", Authentication.authInfo.imsOrgId)

        val call: Call<JsonElement> = API.getDatasetService().retrieveDatasets(headers)
        val response: Response<JsonElement> = call.execute()

        if (response.isSuccessful && response.body() != null) {
            val respBody = response.body()!!.asJsonObject

        }
    }

}
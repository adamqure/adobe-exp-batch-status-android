package com.example.adobe_exp_batch_status_android.Datasets

import com.example.adobe_exp_batch_status_android.API
import com.example.adobe_exp_batch_status_android.Authentication
import com.example.adobe_exp_batch_status_android.Objects.Dataset
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DatasetsModel(var presenterCallback: DatasetsContract.DatasetPresenterInterface) : DatasetsContract.DatasetModelInterface {
    var start = 0
    var datasets = ArrayList<Dataset>()

    init {
        datasets.clear()
        start = 0
    }

    override fun retrieveDatasets() {
        val headers = HashMap<String, String>()
        headers.put("Authorization", "Bearer " + Authentication.authInfo.accessToken)
        headers.put("x-api-key", Authentication.authInfo.apiKey)
        headers.put("x-gw-ims-org-id", Authentication.authInfo.imsOrgId)

//        val call: Call<JsonElement> = API.getDatasetsService().retrieveDatasets(headers)
//        val response: Response<JsonElement> = call.execute()
//
//        if (response.isSuccessful && response.body() != null) {
//            val respBody = response.body()!!.asJsonObject
//
//        }
        var response: Response<JsonElement>
        val call = API.getDatasetsService().retrieveDatasets(headers, ORDER_BY, start, LIMIT)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>
            ) {
                println("Retrieved Datasets")
                val body = response.body()

                if (body != null) {
                    val batchBody = body.asJsonObject
                    val keys = batchBody.keySet()

                    for (key in keys) {
                        val dataset = batchBody.getAsJsonObject(key)
                        var newDataset = Dataset(dataset.get("name").asString, key, dataset.get("state").asString)

                        datasets.add(newDataset)
                    }

                    start += keys.count()

                    if (keys.count() < LIMIT) {
                        presenterCallback.updateDatasets(datasets)
                    } else {
                        presenterCallback.updateDatasets(datasets)
                        retrieveDatasets()
                    }
                }

            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable?) {
                print("Error: Could not retrieve datasets")
                t?.printStackTrace()
//                presenterCallback.loginFailed()
            }
        })
    }

    companion object {
        const val ORDER_BY = "desc:created"
        const val LIMIT = 20
    }

}
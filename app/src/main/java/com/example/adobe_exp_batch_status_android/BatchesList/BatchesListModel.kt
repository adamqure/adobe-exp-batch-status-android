package com.example.adobe_exp_batch_status_android.BatchesList

import com.example.adobe_exp_batch_status_android.API
import com.example.adobe_exp_batch_status_android.Authentication
import com.example.adobe_exp_batch_status_android.Objects.Batch
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BatchesListModel(var presenterCallback: BatchesListContract.BatchesListPresenterInterface) : BatchesListContract.BatchesListModelInterface {
    var start = 0
    var batches = ArrayList<Batch>()

    init {
        start = 0
        batches.clear()
    }

    override fun retrieveBatches(dataset: String) {
        val headers = HashMap<String, String>()
        headers.put("Authorization", "Bearer " + Authentication.authInfo.accessToken)
        headers.put("x-api-key", Authentication.authInfo.apiKey)
        headers.put("x-gw-ims-org-id", Authentication.authInfo.imsOrgId)

        var response: Response<JsonElement>
        val call = API.getDatasetsService().retrieveBatches(headers, dataset, ORDER_BY, start, LIMIT)

        call.enqueue(object: Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                print("Error: Could not retrieve batches with dataset id: $dataset")
                t?.printStackTrace()
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                println("Retrieved Batches")
                val body = response.body()

                if (body != null) {
                    val batchBody = body.asJsonObject
                    val keys = batchBody.keySet()

                    for (key in keys) {
                        val batch = batchBody.getAsJsonObject(key)

                        val newBatch = Batch(key, batch.get("updated").asLong, batch.get("status").asString)

                        batches.add(newBatch)
                    }

                    start += keys.count()

                    if (keys.count() < LIMIT) {
                        presenterCallback.updateBatches(batches)
                    } else {
                        presenterCallback.updateBatches(batches)
                        retrieveBatches(dataset)
                    }
                }
            }
        })
    }


    companion object {
        const val ORDER_BY = "desc:created"
        const val LIMIT = 20
    }
}
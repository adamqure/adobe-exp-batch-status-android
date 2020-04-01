package com.example.adobe_exp_batch_status_android.Batch

import com.example.adobe_exp_batch_status_android.API
import com.example.adobe_exp_batch_status_android.Authentication
import com.example.adobe_exp_batch_status_android.Objects.BatchDetails
import com.example.adobe_exp_batch_status_android.Objects.BatchError
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BatchDetailsModel(var presenterCallback: BatchDetailsContract.BatchDetailsPresenterInterface) : BatchDetailsContract.BatchDetailsModelInterface {
    var isProcessing = false
    var pollingFrequency: Long = 5
    var pollingid = ""
    var pollingTimer = Timer()

    override fun retrieveBatch(batch: String) {
        val headers = HashMap<String, String>()
        headers.put("Authorization", "Bearer " + Authentication.authInfo.accessToken)
        headers.put("x-api-key", Authentication.authInfo.apiKey)
        headers.put("x-gw-ims-org-id", Authentication.authInfo.imsOrgId)

        var response: Response<JsonElement>
        val call = API.getDatasetsService().getBatchDetails(headers, batch)

        call.enqueue(object: Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                println("Failed to retrieve batch information with batch ID: $batch")
                t.printStackTrace()
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                println("Retrieved Batch Details")
                val body = response.body()

                if (body != null) {
                    val batchBody = body.asJsonObject
                    val keys = batchBody.keySet()

                    if (keys.count() == 1) {
                        val details = batchBody.get(keys.elementAt(0)).asJsonObject

                        var metrics: JsonObject? = null
                        if (details.has("metrics")) {
                            metrics = details.get("metrics").asJsonObject
                        }

                        print("details")
                        if (details.has("errors")) {
                            val errors = details.get("errors").asJsonArray

                        }

                        var startedDetail: Long? = null
                        if (details.has("started")) {
                            startedDetail = details.get("started").asLong
                        }

                        var endedDetails: Long? = null
                        if (details.has("completed")) {
                            endedDetails = details.get("completed").asLong
                        }

                        var inputRecord: Long? = null
                        var outputRecord: Long? = null
                        if (metrics != null) {
                            if (metrics.has("inputRecordCount")) {
                                inputRecord = metrics.get("inputRecordCount").asLong
                            }
                            if (metrics.has("outputRecordCount")) {
                                outputRecord = metrics.get("outputRecordCount").asLong
                            }
                        }


                        var errorArray = ArrayList<BatchError>()
                        if (details.has("errors")) {
                            val errors = details.get("errors").asJsonArray
                            for (error in errors) {
                                val errorData = error.asJsonObject
                                val errorDetails = BatchError(errorData.get("code").asString, errorData.get("description").asString)
                                errorArray.add(errorDetails)
                            }
                        }

                        if (isProcessing(details.get("status").asString)) {
                            if (pollingFrequency < POLLING_MAX) {
                                pollingFrequency += 5
                            }
                            if (!isProcessing) {
                                isProcessing = true
                                startTimer(batch)
                            }
                        } else {
                            isProcessing = false
                            stopTimer()
                        }

                        val batchDetail = BatchDetails(startedDetail, endedDetails, details.get("status").asString, inputRecord, outputRecord, errorArray)
                        presenterCallback.updateBatches(batchDetail)
                    }
                }
            }

        })
    }

    fun isProcessing(status: String) : Boolean {
        return when(status) {
            "success" -> {
                false
            }
            "failure", "failed" -> {
                false
            }
            "aborted", "abandoned", "inactive" -> {
                false
            }
            else -> {
                true
            }
        }
    }

    fun startTimer(id: String) {
        val timerTask = object: TimerTask() {
            override fun run() {
                retrieveBatch(id)
            }

        }
        pollingTimer.scheduleAtFixedRate(timerTask, 0, pollingFrequency)
    }

    fun stopTimer() {
        pollingTimer.cancel()
    }

    companion object {
        const val POLLING_MAX = 300
    }

}
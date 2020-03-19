package com.example.adobe_exp_batch_status_android.Objects

class BatchDetails(
    var startedTime: Int?,
    var endedTime: Int?,
    var status: String,
    var successfulCount: Int?,
    var failedCount: Int?,
    var errors: List<BatchError>
) {
}
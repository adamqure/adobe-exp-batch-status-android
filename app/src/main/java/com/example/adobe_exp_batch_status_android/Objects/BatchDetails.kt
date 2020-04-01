package com.example.adobe_exp_batch_status_android.Objects

class BatchDetails(
    var startedTime: Long?,
    var endedTime: Long?,
    var status: String,
    var successfulCount: Long?,
    var failedCount: Long?,
    var errors: List<BatchError>
) {
}
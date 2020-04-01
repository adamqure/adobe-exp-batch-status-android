package com.example.adobe_exp_batch_status_android.Batch

import android.graphics.Color
import android.icu.text.DateFormat
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adobe_exp_batch_status_android.Objects.BatchDetails
import com.example.adobe_exp_batch_status_android.Objects.BatchError
import com.example.adobe_exp_batch_status_android.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class BatchDetailsFragment : Fragment(), BatchDetailsContract.BatchDetailsFragmentInterface {

    var batchId = ""
    var batch = BatchDetails(0, 0, "", 0, 0, ArrayList<BatchError>())

    lateinit var presenter: BatchDetailsContract.BatchDetailsPresenterInterface

    lateinit var title: TextView
    lateinit var successfulLabel: TextView
    lateinit var failedLabel: TextView
    lateinit var startTimeLabel: TextView
    lateinit var endTimeLabel: TextView
    lateinit var errorCountLabel: TextView
    lateinit var statusLabel: TextView
    lateinit var statusIcon: CardView
    lateinit var errorRecyclerView: RecyclerView
    lateinit var backButton: Button
    lateinit var noErrorsView: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_batch_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        title = view.findViewById(R.id.batch_details_title)
        successfulLabel = view.findViewById(R.id.successful_files_text_view)
        failedLabel = view.findViewById(R.id.failed_files_text_view)
        startTimeLabel = view.findViewById(R.id.start_time_text_view)
        endTimeLabel = view.findViewById(R.id.end_time_text_view)
        errorCountLabel = view.findViewById(R.id.total_errors_text_view)
        statusLabel = view.findViewById(R.id.batch_details_status_label)
        statusIcon = view.findViewById(R.id.batch_details_status_icon)
        errorRecyclerView = view.findViewById(R.id.errors_recycler_view)
        noErrorsView = view.findViewById(R.id.no_errors_text_view)
        backButton = view.findViewById(R.id.batch_details_back_button)

        batchId = BatchDetailsFragmentArgs.fromBundle(arguments!!).batchId

        super.onViewCreated(view, savedInstanceState)

        title.text = batchId

        presenter = BatchDetailsPresenter(this)
        presenter.retrieveBatch(batchId)

        this.initializeRecyclerView()
        this.initializeBackButton()
    }

    fun initializeBackButton() {
        this.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun initializeRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        errorRecyclerView.layoutManager = linearLayoutManager
        val adapter = BatchErrorsRecyclerViewAdapter(batch.errors)
        errorRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    override fun updateBatch(batch: BatchDetails) {
        this.batch = batch

        when(batch.status) {
            "success" -> {
                statusLabel.text = "Success"
                statusIcon.setCardBackgroundColor(Color.GREEN)
            }
            "failure", "failed" -> {
                statusLabel.text = "Failure"
                statusIcon.setCardBackgroundColor(Color.RED)
            }
            "aborted", "abandoned", "inactive" -> {
                statusLabel.text = "Aborted"
                statusIcon.setCardBackgroundColor(Color.GRAY)
            }
            else -> {
                statusLabel.text = "Processing"
                statusIcon.setCardBackgroundColor(Color.YELLOW)
            }
        }

        successfulLabel.text = batch.successfulCount.toString()
        failedLabel.text = batch.failedCount.toString()
        if (batch.startedTime != null) {
            val localDate = Instant.ofEpochMilli(batch.startedTime!!).atZone(ZoneId.systemDefault()).toLocalDateTime()
            val format = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
            startTimeLabel.text = localDate.format(format)
        } else {
            startTimeLabel.text = "--"
        }
        if (batch.endedTime != null) {
            val localDate = Instant.ofEpochMilli(batch.endedTime!!).atZone(ZoneId.systemDefault()).toLocalDateTime()
            val format = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
            endTimeLabel.text = localDate.format(format)
        } else {
            endTimeLabel.text = "--"
        }
        if (batch.errors.isNotEmpty()) {
            noErrorsView.visibility = View.GONE
            errorRecyclerView.visibility = View.VISIBLE
            errorCountLabel.text = batch.errors.size.toString()
        } else {
            errorRecyclerView.visibility = View.GONE
            noErrorsView.visibility = View.VISIBLE
            errorCountLabel.text = "0"
        }
    }
}

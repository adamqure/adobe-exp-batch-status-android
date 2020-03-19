package com.example.adobe_exp_batch_status_android.BatchesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adobe_exp_batch_status_android.Objects.Batch
import com.example.adobe_exp_batch_status_android.Objects.Dataset
import com.example.adobe_exp_batch_status_android.R


class BatchesListFragment : Fragment(), BatchesListContract.BatchesListFragmentInterface {

    lateinit var backButton: Button
    lateinit var title: TextView
    lateinit var searchBar: SearchView
    lateinit var batchListRecyclerView: RecyclerView
    lateinit var batchList: List<Batch>
    lateinit var filteredBatchList: List<Batch>
    lateinit var presenter: BatchesListContract.BatchesListPresenterInterface
    var datasetId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_batches_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.backButton = view.findViewById(R.id.batches_back_button)
        this.title = view.findViewById(R.id.batch_title)
        this.searchBar = view.findViewById(R.id.batches_search_view)
        this.batchListRecyclerView = view.findViewById(R.id.batches_recycler_view)

        super.onViewCreated(view, savedInstanceState)
        batchList = ArrayList<Batch>()
        filteredBatchList = ArrayList<Batch>()
        title.text = datasetId

        presenter = BatchesListPresenter(this)
        presenter.retrieveBatches(datasetId)

        this.initializeSearchBar()
        this.initializeRecyclerView()
        this.initializeBackButton()
    }

    fun initializeSearchBar() {
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                filteredBatchList = batchList.filter {
                    it.id.toLowerCase().contains(newText.toLowerCase())
                }

                batchListRecyclerView.adapter?.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
    }

    fun initializeBackButton() {
        this.backButton.setOnClickListener {

        }
    }

    fun initializeRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        batchListRecyclerView.layoutManager = linearLayoutManager
        val adapter = BatchesListRecyclerViewAdapter(filteredBatchList, this)
        batchListRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun updateBatches(batches: ArrayList<Batch>) {
        this.batchList = batches
        this.filteredBatchList = batches

        batchListRecyclerView.adapter?.notifyDataSetChanged()    }

    override fun batchSelected(batch: Batch) {
        //
    }
}

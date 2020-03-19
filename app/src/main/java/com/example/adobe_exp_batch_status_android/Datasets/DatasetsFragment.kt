package com.example.adobe_exp_batch_status_android.Datasets

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
import com.example.adobe_exp_batch_status_android.Objects.Dataset
import com.example.adobe_exp_batch_status_android.R


class DatasetsFragment : Fragment(), DatasetsContract.DatasetFragmentInterface {

    lateinit var logoutButton: Button
    lateinit var searchBar: SearchView
    lateinit var datasetsRecyclerView: RecyclerView

    lateinit var datasetList: List<Dataset>
    lateinit var filteredDatasetList: List<Dataset>
    lateinit var presenter: DatasetsContract.DatasetPresenterInterface


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_datasets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.datasetsRecyclerView = view.findViewById(R.id.datasets_recycler_view)
        this.logoutButton = view.findViewById(R.id.logout_button)
        this.searchBar = view.findViewById(R.id.datasets_search_view)

        super.onViewCreated(view, savedInstanceState)
        datasetList = ArrayList<Dataset>()
        filteredDatasetList = ArrayList<Dataset>()

        presenter = DatasetPresenter(this)
        presenter.retrieveDatasets()

        this.initializeSearchBar()
        this.initializeLogoutButton()
        this.initializeRecyclerView()
    }

    fun initializeSearchBar() {
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                filteredDatasetList = datasetList.filter {
                    it.id.toLowerCase().contains(newText.toLowerCase()) ||
                            it.name.toLowerCase().contains(newText.toLowerCase())
                }

                datasetsRecyclerView.adapter?.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
    }

    fun initializeRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        datasetsRecyclerView.layoutManager = linearLayoutManager
        val adapter = DatasetsRecyclerViewAdapter(filteredDatasetList, this)
        datasetsRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun initializeLogoutButton() {

    }

    override fun datasetSelected(dataset: Dataset) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateDatasets(datasets: ArrayList<Dataset>) {
        this.datasetList = datasets
        this.filteredDatasetList = datasets

        datasetsRecyclerView.adapter?.notifyDataSetChanged()
    }
}

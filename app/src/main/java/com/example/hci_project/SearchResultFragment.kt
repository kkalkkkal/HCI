package com.example.hci_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hci_project.bean.SearchResult
import com.example.hci_project.dummy.DummySchoolContent

class SearchResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_result, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {

                val searchResults = ArrayList<SearchResult>()
                for (idx in 1..20) {
                    searchResults.add(SearchResult(SearchResult.TYPE_SEARCH, "검색기록 ${idx}", "설명"))
                }
                layoutManager = LinearLayoutManager(context)
                adapter = SearchResultRecyclerViewAdapter(searchResults){
                    if(activity!= null){
                        (activity!! as SearchSchoolActivity).search(it.title)
                    }
                }
            }
        }
        return view
    }

    fun setList(resultList: ArrayList<SearchResult>) {
        if (view != null && view is RecyclerView) {
            (view as RecyclerView).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = SearchResultRecyclerViewAdapter(resultList)
            }
        }
    }
}
package com.example.hci_project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hci_project.bean.SearchResult
import com.example.hci_project.bean.SearchResultManager

class SearchResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the adapter
        if (view is RecyclerView) {
            view.layoutManager = LinearLayoutManager(context)
            SearchResultManager.getInstance().use(context!!) {
                if (it != null && !this.isDetached) {
                    activity?.runOnUiThread {
                        view.adapter = SearchResultRecyclerViewAdapter(it.searchResultList) {
                            if (view.isAttachedToWindow) {
                                when (it.type) {
                                    SearchResult.TYPE_SEARCH -> (activity!! as SearchSchoolActivity).search(
                                        it.title
                                    )
                                    SearchResult.TYPE_SEARCH -> {
                                        val intent =
                                            Intent(context!!, SchoolInfoActivity::class.java)
                                        startActivity(intent)
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
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
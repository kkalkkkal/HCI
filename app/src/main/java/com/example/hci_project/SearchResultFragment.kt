package com.example.hci_project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hci_project.bean.SearchResult
import com.example.hci_project.bean.SearchResultManager

class SearchResultFragment : Fragment() {

    lateinit var msgView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        msgView = view.findViewById(R.id.msg)

        // Set the adapter
        SearchResultManager.getInstance().use(context!!) { it ->
            if (it != null && !this.isDetached) {
                activity?.runOnUiThread {
                    setList(ArrayList(it.searchResultList)) {
                        when (it.type) {
                            SearchResult.TYPE_SEARCH -> (activity!! as SearchSchoolActivity).search(
                                it.title
                            )
                            SearchResult.TYPE_SCHOOL -> {
                                SearchResultManager.getInstance().use(context!!) { manager ->
                                    manager?.apply {
                                        searchResultList.add(it)
                                        save(context!!)
                                    }
                                }
                                val intent =
                                    Intent(context, SchoolInfoActivity::class.java)
                                intent.putExtra("school", it)
                                context!!.startActivity(intent)
                            }
                        }
                    }
                    msgView.text = "${it.searchResultList.size}개의 검색 기록이 있습니다"
                }
            }
        }
    }

    fun setList(resultList: ArrayList<SearchResult>, onClick: ((SearchResult) -> Unit)? = null) {
        msgView.text = "${resultList.size}개의 검색 결과가 있습니다"

        val recylcerView = view?.findViewById<RecyclerView>(R.id.search_result_list)
        recylcerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SearchResultRecyclerViewAdapter(resultList, onClick)
        }
    }
}
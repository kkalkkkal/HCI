package com.example.hci_project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hci_project.bean.School
import com.example.hci_project.bean.SchoolManager
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
        SearchResultManager.getInstance().use(context!!) { manager ->
            if (manager != null && !this.isDetached) {
                var renderSearchHistoryList: (() -> Unit)? = null
                renderSearchHistoryList = {
                    setList(ArrayList(manager.searchResultList), {
                        when (it.type) {
                            SearchResult.TYPE_SEARCH -> (activity!! as SearchSchoolActivity).search(
                                it.title
                            )
                            SearchResult.TYPE_SCHOOL -> {
                                manager?.apply {
                                    searchResultList.add(0, it)
                                    save(context!!)
                                }
                                val intent =
                                    Intent(context, SchoolInfoActivity::class.java)
                                //is attached?
                                if (it.obj != null) {
                                    //yes, attached!
                                    intent.putExtra("school", it.obj as School)
                                    context!!.startActivity(intent)
                                } else {
                                    //not attached. let's find.
                                    SchoolManager.getInstance().use(context!!) { schoolManager ->
                                        val school = schoolManager!!.list.find { target ->
                                            target.name == it.title
                                        }
                                        intent.putExtra("school", school)
                                        context!!.startActivity(intent)
                                    }
                                }
                            }
                        }
                    }) { currentResult ->
                        manager.searchResultList.remove(currentResult)
                        manager.save(context!!)

                        activity?.runOnUiThread(renderSearchHistoryList)
                    }
                    msgView.text = "${manager.searchResultList.size}개의 검색 기록이 있습니다"
                }
                activity?.runOnUiThread(renderSearchHistoryList)
            }
        }
    }

    fun setList(
        resultList: ArrayList<SearchResult>,
        onClick: ((SearchResult) -> Unit)? = null,
        onRemove: ((SearchResult) -> Unit)? = null
    ) {
        msgView.text = "${resultList.size}개의 검색 결과가 있습니다"

        val recylcerView = view?.findViewById<RecyclerView>(R.id.search_result_list)
        recylcerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SearchResultRecyclerViewAdapter(resultList, onClick, onRemove)
        }
    }
}
package com.example.hci_project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.hci_project.bean.CompareSchoolManager
import com.example.hci_project.bean.School

class CompareSchoolListFragment : Fragment() {

    private var selectedList: ArrayList<School> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_compare_school_list, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh()
    }

    fun refresh() {
        view?.apply {
            val startCompareBtn = this.findViewById<Button>(R.id.start_compare)
            val msgView = this.findViewById<TextView>(R.id.msg)
            val recyclerView = this.findViewById<RecyclerView>(R.id.list)

            var adapter = SelectableSchoolAdapter(CompareSchoolManager.list) { selectedList ->
                this@CompareSchoolListFragment.selectedList = selectedList
            }

            if (CompareSchoolManager.list.isEmpty()) {
                msgView.setText("아직 비교 리스트에 아무것도 추가되지 않았습니다")
            } else {
                msgView.setText("${CompareSchoolManager.list.size}개의 비교 대상이 있습니다")
            }

            recyclerView.adapter = adapter

            startCompareBtn.setOnClickListener {
                if (selectedList.size < 2) {
                    Toast.makeText(context!!, "2개 이상을 선택해야 비교할 수 있습니다", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else if (selectedList.size > 6) {
                    Toast.makeText(context!!, "6개까지만 비교할 수 있습니다", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(context, EnhancedCompareSchoolActivity::class.java)
                intent.putExtra("list", selectedList)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }
}
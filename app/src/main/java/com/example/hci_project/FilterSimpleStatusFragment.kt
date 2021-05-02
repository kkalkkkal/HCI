package com.example.hci_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class FilterSimpleStatusFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_simple_status, container, false)
    }

    override fun onStart() {
        super.onStart()

        val recyclerView = view!!.findViewById<RecyclerView>(R.id.set_filter_list)
        recyclerView.adapter = ChipAdapter(
                ChipAdapter.Chip.builder().append("유치원", null).append("공립", null).append("스쿨버스", null).end()
        )
    }

}
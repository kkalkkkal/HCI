package com.example.hci_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.hci_project.bean.FilterSetting

class FilterSimpleStatusFragment : Fragment() {

    private var filterSetting: FilterSetting? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filter_simple_status, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.set_filter_list)
        recyclerView.adapter = ChipAdapter(
            ChipAdapter.Chip.builder().end()
        )
        view.visibility= View.GONE
        return view
    }

    fun updateFilter(filterSetting: FilterSetting?) {
        this.filterSetting = filterSetting
        renderFilterStatus()
    }

    private fun renderFilterStatus() {
        view?.visibility= View.VISIBLE

        filterSetting?.apply {
            val recyclerView = view?.findViewById<RecyclerView>(R.id.set_filter_list)

            val builder = ChipAdapter.Chip.ChipBuilder()
            if (minSchoolSize != 0)
                builder.append("최소 면적: ${minSchoolSize}평", null)
            if (maxKidsPerTeacher != 0)
                builder.append("최대 교사 당 학생수: ${maxKidsPerTeacher}명", null)
            if (maxDistanceKmFromHere != 0)
                builder.append("최대 거리: ${maxDistanceKmFromHere}km", null)

            if (schoolStartHour != 0)
                builder.append("개원 시간: ${schoolStartHour}시 이전", null)
            if (schoolEndTime != 0)
                builder.append("폐원 시간: ${schoolEndTime}시 이후", null)

            facilitates.map {
                builder.append(it.name, null)
            }

            (recyclerView?.adapter as ChipAdapter).apply {
                values= builder.end()
                notifyDataSetChanged()
            }
        }
    }

}
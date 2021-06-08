package com.example.hci_project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hci_project.bean.School
import com.example.hci_project.bean.SchoolManager
import com.example.hci_project.bean.SearchResult
import com.example.hci_project.bean.SearchResultManager

class SelectableSchoolAdapter(
    private var values: List<School>, private val onSelect: ((ArrayList<School>) -> Unit)
) : RecyclerView.Adapter<SelectableSchoolAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val selectedList = ArrayList<School>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_selectable_school, parent, false)
        this.context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val school = values[position]
        if (selectedList.contains(school)) {
            //remove
        } else {
            selectedList.add(school)
        }

        holder.apply {
            itemView.setOnClickListener {
                checkBoxView.isChecked= !checkBoxView.isChecked
            }
            checkBoxView.setOnCheckedChangeListener { buttonView, isChecked ->
                if (selectedList.contains(school)) {
                    //remove
                    val iterator = selectedList.iterator()
                    while (iterator.hasNext()) {
                        if (iterator.next().equals(school)) {
                            iterator.remove()
                            break;
                        }
                    }
                } else {
                    selectedList.add(school)
                }

                onSelect(selectedList)
            }
            titleView.setText(school.name)
            typeView.setText("[%s]".format(school.getServiceType()))
        }
    }


    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.title)
        val typeView: TextView = view.findViewById(R.id.type)
        val checkBoxView: CheckBox = view.findViewById(R.id.checkbox)
    }
}
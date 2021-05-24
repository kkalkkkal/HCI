package com.example.hci_project

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.hci_project.dummy.DummySchoolContent.DummyItem

class SearchResultRecyclerViewAdapter(
        private val values: List<DummyItem>)
    : RecyclerView.Adapter<SearchResultRecyclerViewAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_search_result, parent, false)
        this.context= parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.titleView.text = item.content
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, SchoolInfoActivity::class.java))
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.title)

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }
}
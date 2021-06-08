package com.example.hci_project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hci_project.bean.SearchResult
import com.example.hci_project.bean.SearchResultManager
import java.lang.IllegalStateException

class CompareEntryAdapter(
    private val titles: List<String>,
    private val values: List<List<MaterialItemAdapter.MaterialItem>>,
) : RecyclerView.Adapter<CompareEntryAdapter.ViewHolder>() {
    class Builder(@ColorInt val colors: Array<Int>) {
        private var index = 0
        private var list: ArrayList<MaterialItemAdapter.MaterialItem> = ArrayList()

        fun append(item: MaterialItemAdapter.MaterialItem) {
            item.tint = colors[index]
            index++
            list.add(item)
        }

        fun build(): ArrayList<MaterialItemAdapter.MaterialItem> {
            val result = list
            flush()
            return result
        }

        fun flush() {
            index = 0
            list = ArrayList()
        }
    }

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_compare_entry, parent, false)
        this.context = parent.context

        if (titles.size != values.size) {
            throw IllegalStateException("titles's size and values' size is not same!")
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = titles[position]
        val compares = values[position]
        holder.apply {
            titleView.setText(title)
            recyclerView.adapter = MaterialItemAdapter(compares)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.compare_title)
        val recyclerView: RecyclerView = view.findViewById(R.id.compare_entry)
    }
}
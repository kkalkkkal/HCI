package com.example.hci_project

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.hci_project.bean.SearchResult
import com.example.hci_project.bean.SearchResultManager

import com.example.hci_project.dummy.DummySchoolContent.DummyItem

class SearchResultRecyclerViewAdapter(
    private val values: List<SearchResult>, private val onClick: ((SearchResult) -> Unit)? = null
) : RecyclerView.Adapter<SearchResultRecyclerViewAdapter.ViewHolder>() {


    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_search_result, parent, false)
        this.context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.titleView.text = item.title
        holder.explainView.visibility = View.GONE
        if (item.subtitle != null) {
            holder.explainView.text = item.subtitle
            holder.explainView.visibility = View.VISIBLE
        }
        holder.iconView.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                when (item.type) {
                    SearchResult.TYPE_SEARCH -> R.drawable.ic_baseline_search_24
                    SearchResult.TYPE_SCHOOL -> R.drawable.ic_baseline_apartment_24
                    else -> R.drawable.ic_baseline_search_24
                }
            )
        )

        holder.itemView.setOnClickListener {
            if (onClick != null) {
                onClick.run {
                    this(item)
                }
            } else {
                SearchResultManager.getInstance().use(context) { manager ->
                    manager?.apply {
                        searchResultList.add(item)
                        save(context)
                    }
                }
                val intent =
                    Intent(context, SchoolInfoActivity::class.java)
                intent.putExtra("school", item)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.title)
        val explainView: TextView = view.findViewById(R.id.explain)
        val iconView: ImageView = view.findViewById(R.id.icon)

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }
}
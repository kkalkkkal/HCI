package com.example.hci_project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hci_project.bean.School
import com.example.hci_project.bean.SchoolManager
import com.example.hci_project.bean.SearchResult
import com.example.hci_project.bean.SearchResultManager

class SearchResultRecyclerViewAdapter(
    private val values: List<SearchResult>, private val onClick: ((SearchResult) -> Unit)? = null,
    private val onLongClick: ((SearchResult) -> Unit)? = null
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
                        searchResultList.add(0, item)
                        save(context)
                    }
                }
                val intent =
                    Intent(context, SchoolInfoActivity::class.java)
                //is attached?
                if (item.obj != null) {
                    //yes, attached!
                    intent.putExtra("school", item.obj as School)
                    context.startActivity(intent)
                } else {
                    //not attached. let's find.
                    SchoolManager.getInstance().use(context) { schoolManager ->
                        val school = schoolManager!!.list.find { target ->
                            target.name == item.title
                        }
                        intent.putExtra("school", school)
                        context.startActivity(intent)
                    }
                }
            }
        }
        if (onLongClick != null) {
            holder.removeBtn.visibility = View.VISIBLE
            holder.removeBtn.setOnClickListener {
                onLongClick.run {
                    this(item)
                }
            }
        } else {
            holder.removeBtn.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.title)
        val explainView: TextView = view.findViewById(R.id.explain)
        val iconView: ImageView = view.findViewById(R.id.icon)
        val removeBtn: ImageView = view.findViewById(R.id.removeBtn)

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }
}
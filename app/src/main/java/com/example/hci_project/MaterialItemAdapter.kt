package com.example.hci_project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hci_project.bean.SearchResult
import com.example.hci_project.bean.SearchResultManager

class MaterialItemAdapter(
    private val values: List<MaterialItem>
) : RecyclerView.Adapter<MaterialItemAdapter.ViewHolder>() {
    class MaterialItem(
        val icon: Int,
        val explain: String,
        val onClick: (() -> Unit)? = null
    ) {
        constructor(
            headerText: String,
            icon: Int,
            explain: String,
            onClick: (() -> Unit)? = null
        ) : this(icon, explain, onClick) {
            this.headerText = headerText
        }

        @ColorRes
        var tint: Int? = null

        var headerText: String? = null
    }

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_material_item, parent, false)
        this.context = parent.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        holder.explainView.text = item.explain
        holder.iconView.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                item.icon
            )
        )
        if (item.tint != null) {
            try {
                holder.iconView.setColorFilter(ContextCompat.getColor(context, item.tint!!))
            } catch (e: Exception) {
                holder.iconView.setColorFilter(item.tint!!)
            }
        } else {
            holder.iconView.setColorFilter(ContextCompat.getColor(context, R.color.yellow))
        }

        if (item.headerText != null) {
            holder.headerView.apply {
                visibility = View.VISIBLE
                setText(item.headerText)
                if (item.tint != null) {
                    try {
                        setTextColor(ContextCompat.getColor(context, item.tint!!))
                    } catch (e: Exception) {
                        setTextColor(item.tint!!)
                    }
                }
            }
        } else {
            holder.headerView.apply {
                visibility = View.GONE
                setTextColor(ContextCompat.getColor(context, R.color.gray_light))
            }

        }

        holder.itemView.setOnClickListener {
            if (item.onClick != null) {
                item.onClick.run {
                    this()
                }
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val explainView: TextView = view.findViewById(R.id.explain)
        val iconView: ImageView = view.findViewById(R.id.icon)
        val headerView: TextView = view.findViewById(R.id.header_text)
    }
}
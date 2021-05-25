package com.example.hci_project

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChipAdapter(
        var values: List<Chip>)
    : RecyclerView.Adapter<ChipAdapter.ViewHolder>() {

    private val colors= arrayOf<@androidx.annotation.ColorInt Int>(
            Color.rgb(3, 169, 244),
            Color.rgb(244, 67, 54),
            Color.rgb(233, 30, 99),
            Color.rgb(156, 39, 176),
            Color.rgb(0, 150, 136),
            Color.rgb(255, 152, 0)
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_chip, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.chipTV.text = item.label
        holder.chipTV.setOnClickListener {
            item.onClick?.run()
        }

        holder.itemView.background.setTint(colors[position%colors.size])
        holder.chipTV.setTextColor(Color.WHITE)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chipTV: TextView = view.findViewById(R.id.chip_label)
    }

    class Chip(var label: String, var onClick: Runnable?) {

        class ChipBuilder {
            private val chips: MutableList<Chip> = ArrayList()
            fun append(label: String, onClick: Runnable?): ChipBuilder {
                chips.add(Chip(label, onClick))
                return this
            }

            fun end(): MutableList<Chip> {
                return chips
            }
        }

        companion object {
            fun builder(): ChipBuilder {
                return ChipBuilder()
            }
        }
    }
}
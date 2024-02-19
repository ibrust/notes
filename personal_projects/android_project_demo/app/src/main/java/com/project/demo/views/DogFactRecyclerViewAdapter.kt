package com.project.demo.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.demo.R

class DogFactCellData(val title: String, val subText: String)

class DogFactRecyclerViewAdapter(
    private val dogFactsList: List<DogFactCellData>
) : RecyclerView.Adapter<DogFactRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.dog_fact_card,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dogFactCellData = dogFactsList[position]
        holder.titleView.text = dogFactCellData.title
        holder.subTextView.text = dogFactCellData.subText
    }

    override fun getItemCount(): Int {
        return dogFactsList.size
    }

    class ViewHolder(cellView: View) : RecyclerView.ViewHolder(cellView) {
        val titleView: TextView = cellView.findViewById(R.id.title)
        val subTextView: TextView = cellView.findViewById(R.id.subtext)
    }
}
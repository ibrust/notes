package com.project.demo.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.demo.R

class HomeScreenSectionsData(val title: String, val subText: String)

class HomeScreenRecyclerViewAdapter(
    private val homeScreenPanelsData: List<HomeScreenSectionsData>
) : RecyclerView.Adapter<HomeScreenRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.home_section_card,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cellData = homeScreenPanelsData[position]
        holder.titleView.text = cellData.title
        holder.subTextView.text = cellData.subText
    }

    override fun getItemCount(): Int {
        return homeScreenPanelsData.size
    }

    class ViewHolder(cellView: View) : RecyclerView.ViewHolder(cellView) {
        val titleView: TextView = cellView.findViewById(R.id.title)
        val subTextView: TextView = cellView.findViewById(R.id.subtext)
    }
}
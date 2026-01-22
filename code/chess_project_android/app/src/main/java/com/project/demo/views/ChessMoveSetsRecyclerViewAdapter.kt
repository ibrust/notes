package com.project.demo.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.demo.R

class ChessMovesCellData(val title: String, val subText: String)

class ChessMoveSetsRecyclerViewAdapter(
    private val chessMovesList: List<ChessMovesCellData>
) : RecyclerView.Adapter<ChessMoveSetsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.chess_move_set_card,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chessMovesCellData = chessMovesList[position]
        holder.titleView.text = chessMovesCellData.title
        holder.subTextView.text = chessMovesCellData.subText
    }

    override fun getItemCount(): Int {
        return chessMovesList.size
    }

    class ViewHolder(cellView: View) : RecyclerView.ViewHolder(cellView) {
        val titleView: TextView = cellView.findViewById(R.id.title)
        val subTextView: TextView = cellView.findViewById(R.id.subtext)
    }
}
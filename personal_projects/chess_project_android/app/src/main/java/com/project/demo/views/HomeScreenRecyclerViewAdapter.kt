package com.project.demo.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.demo.R
import com.project.demo.models.ChessPiece
import com.project.demo.models.GameState
import kotlin.math.abs

class HomeScreenCellData(
    val playButtonData: ArrayList<PlayButtonData> = arrayListOf(),
    val recentGamesData: ArrayList<RecentGamesData> = arrayListOf()
) {
    class PlayButtonData(
        val resId: Int?,
        val title: String
    )
    class RecentGamesData(
        val result: GameState,
        val positions: ArrayList<ArrayList<ChessPiece?>>
    )
}

class HomeScreenRecyclerViewAdapter(
    private val homeScreenCellData: HomeScreenCellData
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0, 1, 2 -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.home_screen_play_buttons,
                    parent,
                    false
                )
                return PlayButtonsViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.home_screen_recent_games,
                    parent,
                    false
                )
                return RecentGamesViewHolder(view)
            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0, 1, 2 -> {
                if (homeScreenCellData.playButtonData.size <= position) return
                val cellData = homeScreenCellData.playButtonData[position]
                val buttonsViewHolder = holder as? PlayButtonsViewHolder ?: return
                buttonsViewHolder.titleView.text = cellData.title
                val resId = cellData.resId ?: return
                buttonsViewHolder.imageView.setBackgroundResource(resId)
            }
            else -> {
                val offsetPosition = abs(position - 3)
                if (homeScreenCellData.recentGamesData.size <= offsetPosition) return
                val cellData = homeScreenCellData.recentGamesData[offsetPosition]
                val recentGamesViewHolder = holder as? RecentGamesViewHolder ?: return
            }
        }
    }

    override fun getItemCount(): Int {
        return homeScreenCellData.playButtonData.size + homeScreenCellData.recentGamesData.size
    }

    class RecentGamesViewHolder(cellView: View) : RecyclerView.ViewHolder(cellView) {
        val titleView: TextView = cellView.findViewById(R.id.home_button_title)
    }

    class PlayButtonsViewHolder(cellView: View) : RecyclerView.ViewHolder(cellView) {
        val imageView: TextView = cellView.findViewById(R.id.home_button_image_view)
        val titleView: TextView = cellView.findViewById(R.id.home_button_title)
    }
}
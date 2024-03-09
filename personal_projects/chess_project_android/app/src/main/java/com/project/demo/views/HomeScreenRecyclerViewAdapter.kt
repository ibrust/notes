package com.project.demo.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.demo.R

class HomeScreenSectionsData(
    val resId: Int?,
    val title: String
)

class HomeScreenRecyclerViewAdapter(
    private val homeScreenPanelsData: List<HomeScreenSectionsData>
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
                return SectionsViewHolder(view)
            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cellData = homeScreenPanelsData[position]
        when (holder.itemViewType) {
            0, 1, 2 -> {
                val buttonsViewHolder = holder as? PlayButtonsViewHolder ?: return
                buttonsViewHolder.titleView.text = cellData.title
                val resId = cellData.resId ?: return
                buttonsViewHolder.imageView.setBackgroundResource(resId)
            }
            else -> {

            }
        }
    }

    override fun getItemCount(): Int {
        return homeScreenPanelsData.size
    }

    class SectionsViewHolder(cellView: View) : RecyclerView.ViewHolder(cellView) {
        val titleView: TextView = cellView.findViewById(R.id.home_button_title)
    }

    class PlayButtonsViewHolder(cellView: View) : RecyclerView.ViewHolder(cellView) {
        val imageView: TextView = cellView.findViewById(R.id.home_button_image_view)
        val titleView: TextView = cellView.findViewById(R.id.home_button_title)
    }
}
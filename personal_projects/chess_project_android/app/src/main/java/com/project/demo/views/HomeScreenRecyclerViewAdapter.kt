package com.project.demo.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.project.demo.R
import com.project.demo.models.ChessPiece
import com.project.demo.models.GameState
import java.lang.ref.WeakReference


open class HomeScreenCellData()
class PlayButtonData(
    val resId: Int,
    val title: String
) : HomeScreenCellData()
class RecentGamesData(
    val result: GameState,
    val positions: ArrayList<ArrayList<ChessPiece?>>
) : HomeScreenCellData()

interface HomeScreenRecyclerViewDelegate {
    fun didClickPlayButton(position: Int)
}

class HomeScreenRecyclerViewAdapter(
    private val homeScreenCellData: ArrayList<HomeScreenCellData>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var delegate: WeakReference<HomeScreenRecyclerViewDelegate>? = null

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
        if (homeScreenCellData.size <= position) return
        when (holder.itemViewType) {
            0, 1, 2 -> {
                val playButtonData = homeScreenCellData[position] as? PlayButtonData ?: return
                val buttonsViewHolder = holder as? PlayButtonsViewHolder ?: return
                buttonsViewHolder.titleView.text = playButtonData.title
                buttonsViewHolder.imageView.setBackgroundResource(playButtonData.resId)
                buttonsViewHolder.cardView.setOnClickListener {
                    didClickPlayButton(position)
                }
            }
            else -> {
                val recentGamesData = homeScreenCellData[position] as? RecentGamesData ?: return
                val recentGamesViewHolder = holder as? RecentGamesViewHolder ?: return
            }
        }
    }

    fun didClickPlayButton(position: Int) {
        delegate?.get()?.didClickPlayButton(position)
    }

    override fun getItemCount(): Int {
        return homeScreenCellData.size
    }

    class RecentGamesViewHolder(cellView: View) : RecyclerView.ViewHolder(cellView) {
        val titleView: TextView = cellView.findViewById(R.id.home_button_title)
    }

    class PlayButtonsViewHolder(cellView: View) : RecyclerView.ViewHolder(cellView) {
        val imageView: ImageView = cellView.findViewById(R.id.home_button_image_view)
        val titleView: TextView = cellView.findViewById(R.id.home_button_title)
        val cardView: CardView = cellView.findViewById(R.id.play_button_cardview)
    }
}
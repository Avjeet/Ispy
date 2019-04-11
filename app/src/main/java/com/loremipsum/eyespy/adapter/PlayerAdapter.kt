package com.loremipsum.eyespy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loremipsum.eyespy.R
import kotlinx.android.synthetic.main.player_list_item.view.*


/**
 * Created by Your name on 18-11-2018.
 */

class PlayerAdapter(private val playerList: ArrayList<String>,val context : Context) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.player_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvPlayer?.text = playerList[position]
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvPlayer = view.tv_player_name
}
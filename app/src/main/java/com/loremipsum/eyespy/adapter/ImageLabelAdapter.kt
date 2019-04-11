package com.loremipsum.eyespy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loremipsum.eyespy.R
import kotlinx.android.synthetic.main.item_row.view.*

class ImageLabelAdapter(private var firebaseVisionList: List<HashMap<String, String>>) : RecyclerView.Adapter<ImageLabelAdapter.ItemHolder>() {

    companion object {
        const val LABEL = "label"
        const val CONFIDENCE = "confidence"
    }

    lateinit var context: Context

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindCurrent(dataDictionary: HashMap<String, String>) {
            itemView.itemAccuracy.text = "Probability: ${dataDictionary[CONFIDENCE]} %"
            itemView.itemName.text = dataDictionary[LABEL]
        }
    }

    fun setList(visionList: List<HashMap<String, String>>) {
        firebaseVisionList = visionList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val currentItem = firebaseVisionList[position]
        holder.bindCurrent(currentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        context = parent.context
        return ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_row, parent, false))
    }

    override fun getItemCount() = firebaseVisionList.size
}
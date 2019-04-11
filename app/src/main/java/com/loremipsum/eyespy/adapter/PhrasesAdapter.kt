package com.loremipsum.eyespy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loremipsum.eyespy.R
import com.loremipsum.eyespy.activity.ImageLabelActivity
import com.loremipsum.eyespy.model.Phrases
import kotlinx.android.synthetic.main.phrases_item.view.*

/**
 * Created by Your name on 18-11-2018.
 */

class PhrasesAdapter(private val phraseList: ArrayList<Phrases>, val context : Context) : RecyclerView.Adapter<PhrasesViewHolder>() {
    override fun onBindViewHolder(holder: PhrasesViewHolder, position: Int) {
        holder.tvPhrase?.text = "${position}. ${phraseList.get(position).displayText}"
        holder.cvCard.setOnClickListener {
            val intent = Intent(context,ImageLabelActivity::class.java)
            intent.putExtra(ImageLabelActivity.PHRASE_EXTRA, phraseList[position])
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhrasesViewHolder {
        return PhrasesViewHolder(LayoutInflater.from(context).inflate(R.layout.phrases_item, parent, false))
    }

    override fun getItemCount(): Int {
        return phraseList.size
    }
}

class PhrasesViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvPhrase = view.phrase_text
    val cvCard = view.cvCard
}
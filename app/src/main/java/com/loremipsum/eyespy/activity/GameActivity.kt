package com.loremipsum.eyespy.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.loremipsum.eyespy.R
import com.loremipsum.eyespy.adapter.PhrasesAdapter
import com.loremipsum.eyespy.client.RestApiClient
import com.loremipsum.eyespy.model.Phrases
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : AppCompatActivity() {



    lateinit var phrases: ArrayList<Phrases>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

//        val phrasesCall = RestApiClient.restClientService.getPhrases()
//        phrases = phrasesCall.execute().body()!!
        phrases = ArrayList()
        phrases.add(Phrases("qwertyuiopasdfghjklzxcvbnm", arrayListOf()) )
        phrases.add(Phrases("qwertyuiopasdfghjklzxcvbnm", arrayListOf()))
        phrases.add(Phrases("qwertyuiopasdfghjklzxcvbnm", arrayListOf()))


        rvPhrases.layoutManager = LinearLayoutManager(this)
        rvPhrases.adapter = PhrasesAdapter(phrases,this)

    }
}

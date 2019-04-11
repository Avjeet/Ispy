package com.loremipsum.eyespy.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.loremipsum.eyespy.R
import com.loremipsum.eyespy.adapter.PlayerAdapter
import com.loremipsum.eyespy.client.RestApiClient
import com.loremipsum.eyespy.model.Phrases
import kotlinx.android.synthetic.main.activity_game_room.*
import retrofit2.Callback

class GameRoomActivity : AppCompatActivity() {

    companion object {
        val ROOM_NAME_KEY = "room_name_key"
    }

    val players : ArrayList<String> = ArrayList()

    lateinit var phrases: ArrayList<Phrases>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_room)

        rvPlayer.layoutManager = LinearLayoutManager(this)
        rvPlayer.adapter = PlayerAdapter(players,this)
        readyButton.setOnClickListener {
            startActivity(Intent(this@GameRoomActivity,GameActivity::class.java))
        }

    }


}

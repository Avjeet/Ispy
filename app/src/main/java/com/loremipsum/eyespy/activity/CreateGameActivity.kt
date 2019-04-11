package com.loremipsum.eyespy.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.loremipsum.eyespy.R
import kotlinx.android.synthetic.main.activity_create_game.*


class CreateGameActivity : AppCompatActivity() {

    companion object {
        const val CREATE_GAME_KEY = "Create new Room"
        const val JOIN_GAME_KEY = "Join Room"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_game)

        createGame.setOnClickListener {
            createRoomNameDialog(CREATE_GAME_KEY)
        }

        joinGame.setOnClickListener {
            createRoomNameDialog(JOIN_GAME_KEY)
        }
    }

    fun createRoomNameDialog(key: String) {
        val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.room_name_custom_dialog, null, false)
        val roomNameEditText = dialogView.findViewById<EditText>(R.id.roomNameEditText)
        val dialog = AlertDialog.Builder(this).setTitle(key)
                .setView(dialogView).setPositiveButton("Go") { dialogInterface: DialogInterface?, i: Int ->
                    val roomName: String = roomNameEditText.text.toString()
                    when (key) {
                        CREATE_GAME_KEY -> createNewRoom(roomName)
                        JOIN_GAME_KEY -> joinExisitngRoom(roomName)
                    }
                }.create()
        dialog.show()
    }

    fun createNewRoom(key: String) {
        startActivity(Intent(this@CreateGameActivity,GameRoomActivity::class.java))
    }

    fun joinExisitngRoom(key: String) {
        Toast.makeText(this@CreateGameActivity, key, Toast.LENGTH_LONG).show()
    }


    fun startGameRoomActivity(roomName: String) {
        val intent = Intent(this, GameRoomActivity::class.java)
        intent.putExtra(GameRoomActivity.ROOM_NAME_KEY, roomName)
        startActivity(intent)
    }
}

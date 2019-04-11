package com.loremipsum.eyespy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class User(
        var ready: Boolean = false,
        val username: String = ""
)

data class Room(
        val users: ArrayList<User>,
        val status: Boolean = true,
        val roomName: String = ""
)

@Parcelize
data class Phrases(
        val displayText: String,
        val labels: ArrayList<String>
) : Parcelable

data class Dataset(
        val path: String,
        val labels: ArrayList<String>
)
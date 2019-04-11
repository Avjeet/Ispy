package com.loremipsum.eyespy.client

import com.loremipsum.eyespy.SOCKET_SERVER_URL
import io.socket.client.IO
import io.socket.client.Socket

class SocketClient {
    private lateinit var socket: Socket

    fun connectSocket(roomName: String): Socket {
        if (!this::socket.isInitialized) {
            createSocket(roomName)
        }
        socket.connect()
        return socket
    }

    private fun createSocket(roomName: String) {
        socket = IO.socket("$SOCKET_SERVER_URL/$roomName")
    }

    private fun disconnectSocket() {
        socket.disconnect()
        socket.off()
    }
}
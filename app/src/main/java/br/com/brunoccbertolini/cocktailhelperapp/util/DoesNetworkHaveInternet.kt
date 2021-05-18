package br.com.brunoccbertolini.cocktailhelperapp.util

import android.content.ContentValues.TAG
import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object DoesNetworkHaveInternet {

    fun execute(): Boolean {
        return try{
            Log.d(TAG, "PING GOOGLE")
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53),
            1500)
            socket.close()
            Log.d(TAG, "PING Success.")
            true
        }catch (e:IOException){
            Log.e(TAG, "NO internet connection. ")
            false
        }
    }
}
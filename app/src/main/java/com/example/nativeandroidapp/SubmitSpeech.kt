package com.example.nativeandroidapp

import android.util.Log
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SubmitSpeech {
    fun submit(text: String) {
            val url = URL("http://192.168.0.12:3000/")
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            val json = "{\"text\":\"$text\"}"
            urlConnection.requestMethod = "POST"
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");

            try {
                urlConnection.doOutput = true
                urlConnection.setChunkedStreamingMode(0)
                val os = urlConnection.outputStream
                val jsonParam = JSONObject(json)

                os.write(jsonParam.toString().toByteArray())
                os.flush();
                os.close();

                Log.i("** STATUS", urlConnection.responseCode.toString())
                Log.i("** MSG" , urlConnection.responseMessage);

                urlConnection.disconnect();

            } catch(e: java.lang.Exception) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect()
            }
    }

}
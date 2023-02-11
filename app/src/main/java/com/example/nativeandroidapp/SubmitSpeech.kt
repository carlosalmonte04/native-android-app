package com.example.nativeandroidapp

import android.util.Log
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class SubmitSpeech {
    fun submit(text: String) {
            val url = URL("http://192.168.0.12:3000/")
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            try {
                urlConnection.setDoOutput(true)
                urlConnection.setChunkedStreamingMode(0)
                val os = DataOutputStream(urlConnection.getOutputStream())
                val jsonParam = JSONObject()
                jsonParam.put("body", text)
                os.writeBytes(jsonParam.toString())
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
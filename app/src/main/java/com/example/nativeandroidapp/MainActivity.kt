package com.example.nativeandroidapp

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequestBuilder
import com.example.nativeandroidapp.ui.theme.NativeAndroidAppTheme
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.net.URLEncoder
import java.util.*


class MainActivity : ComponentActivity() {
    val RecordAudioRequestCode = 1
    private var speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
    private var speechRecognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    private lateinit var editText: EditText
    private lateinit var micButton: ImageButton
    private var isListening: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val saveRequest =
            PeriodicWorkRequestBuilder<{this.onClickAction()}>(3, TimeUnit.SECONDS)
                // Additional configuration
                .build()

        setContentView(com.example.nativeandroidapp.R.layout.activity_main)
//        setContent {
//            Greeting(
//                "Brian",
//                fun(): Unit {
//                    onClickAction()
//                }
//            )
//        }

        editText = findViewById(com.example.nativeandroidapp.R.id.text)
        micButton = findViewById(com.example.nativeandroidapp.R.id.button)
        micButton.setImageResource(R.drawable.ic_btn_speak_now)
        micButton.setOnClickListener { this.onClickAction() }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermission()
        }
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle) {
                println("[LOGS]: ON READY FOR SPEECH")
            }
            override fun onBeginningOfSpeech() {
                println("[LOGS]: ON BEGINNING OF SPEECH")
                editText.setText("")
                editText.setHint("Please record audio...")
            }

            override fun onRmsChanged(v: Float) {
                println("[LOGS]: RMS CHANGED $v")
            }
            override fun onBufferReceived(bytes: ByteArray) {
                println("[LOGS]: BUFFER RECEIVED $bytes")
            }
            override fun onEndOfSpeech() {
                println("[LOGS]: END OF SPEECH")
            }
            override fun onError(i: Int) {
                println("[LOGS]: ON SPEECH ERROR $i")

            }
            override fun onResults(bundle: Bundle) {
                micButton.setImageResource(R.drawable.ic_btn_speak_now)
                val data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                editText.setText(data!![0])
                println("[LOGS]: GOT RESULTS ${data!![0]}")
                val SpeechManager = SubmitSpeech();
                SpeechManager.submit(data!![0]);
            }

            private fun readStream(input: InputStream) {
                println("** read stream");
                println(input);
            }

            private fun writeStream(out: OutputStream) {
                println("** write stream");
                println(out);
            }

            override fun onPartialResults(bundle: Bundle) {
                println("[LOGS]: GOT PARTIAL RESULTS")
            }
            override fun onEvent(i: Int, bundle: Bundle) {
                println("[LOGS]: ON SPEECH EVENT $i")
            }
        })
    }

    private fun onClickAction(): Unit {
        println("[LOGS]: I WAS PRESSED! $isListening")
        if (isListening) {
            println("[LOGS]: stopped listening")
            speechRecognizer.stopListening()
        }
        else {
            println("[LOGS]: started listening")
            speechRecognizer.startListening(speechRecognizerIntent)
        }
        println("[LOGS]: IS LISTENING VALUE $isListening")
        isListening = !isListening
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer!!.destroy()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RecordAudioRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RecordAudioRequestCode && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) Toast.makeText(
                this,
                "Permission Granted",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun sendReq(text: String) {
        var reqParam = URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(text, "UTF-8")
        val mURL = URL("http://localhost:3000/")
    }
}

@Composable
fun Greeting(name: String, callback: () -> Unit) {
    Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
        Button(
            content = {
                Text(
                    "Hello $name!",
                    color = Color.Black,
                    modifier = Modifier
                        .layoutId(R.id.text1)
                )
            },
            onClick = {callback},
            modifier = Modifier.layoutId(R.id.button1)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NativeApp() {
    NativeAndroidAppTheme {

//        setContentView(com.example.nativeandroidapp.R.layout.activity_main)
//        com.example.nativeandroidapp.R.layout.activity_main
        Greeting("Stewie", fun(): Unit {print("Callback called")})
    }
}
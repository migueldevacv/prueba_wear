package com.example.wear1

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Debug
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private var transcriptionNodeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text: TextView = findViewById(R.id.text)

        val btn: Button = findViewById(R.id.button)
        btn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                transcriptionNodeId = getNodes().first()?.also { nodeId->
                    val sendTask: Task<*> = Wearable.getMessageClient(applicationContext).sendMessage(
                        nodeId,
                        MESSAGE_PATH,
                        "deploy".toByteArray() //send your desired information here
                    ).apply {
                        addOnSuccessListener { Log.d(TAG, "OnSuccess") }
                        addOnFailureListener { Log.d(TAG, "OnFailure") }
                    }
                }

            }
        }
    }

    companion object{
        private const val TAG = "MainWearActivity"
        private const val MESSAGE_PATH = "/deploy"
    }
    private fun getNodes(): Collection<String> {
        return Tasks.await(Wearable.getNodeClient(this).connectedNodes).map { it.id }
    }
}

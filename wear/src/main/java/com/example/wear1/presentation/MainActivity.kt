/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.wear1.presentation

import androidx.lifecycle.lifecycleScope
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.wear1.R
import com.example.wear1.presentation.theme.Wear1Theme
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var transcriptionNodeId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp("Android")
        }


        connectAndSendMessage()
    }

    fun connectAndSendMessage() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Get the connected nodes
            val connectedNodes = Tasks.await(Wearable.getNodeClient(application).connectedNodes)

            // Check if there are any connected nodes
            if (connectedNodes.isNotEmpty()) {
                transcriptionNodeId = connectedNodes.first().id

                // Send message to the first connected node
                val sendTask: Task<*> = Wearable.getMessageClient(application)
                    .sendMessage(transcriptionNodeId!!, MESSAGE_PATH, "deploy".toByteArray())
                    .apply {
                        addOnSuccessListener { Log.d(TAG, "Message sent successfully") }
                        addOnFailureListener { Log.d(TAG, "Error sending message", it) }
                    }
            } else {
                Log.d(TAG, "No nodes connected")
            }
        }
    }

    companion object{
        private const val TAG = "MainWearActivity"
        private const val MESSAGE_PATH = "/deploy"
    }
    private fun getNodes(): Collection<String> {
        return Tasks.await(Wearable.getNodeClient(this@MainActivity).connectedNodes).map { it.id }
    }
}

@Composable

fun WearApp(greetingName: String) {
    Wear1Theme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Greeting(greetingName = greetingName)
            Button(
                onClick = {
                    // MainActivity().connectAndSendMessage()
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Connect and Send Message")
            }
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        text = stringResource(R.string.hello_world, greetingName),
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}
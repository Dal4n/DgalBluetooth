/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.dgalconnection.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.example.dgalconnection.R
import com.example.dgalconnection.presentation.theme.DgalConnectionTheme
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

class MainActivity : ComponentActivity() {

    companion object {
        private var instance: MainActivity? = null

        fun getInstance(): MainActivity? {
            return instance
        }
    }

    private lateinit var dataClient: DataClient
    public var counter = 0
    private lateinit var contadorNum: TextView
    private lateinit var conection: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        super.onCreate(savedInstanceState)
        instance = this
        setTheme(android.R.style.Theme_DeviceDefault)
        setContentView(R.layout.activity_main)
        dataClient = Wearable.getDataClient(this)
        conection = findViewById<TextView>(R.id.conection)
        contadorNum = findViewById<TextView>(R.id.contador)
        val btnAumentar = findViewById<Button>(R.id.btn_Aumentar)
        btnAumentar.setOnClickListener{
            incrementCounter()
        }
    }

    fun updateCounter() {
        Log.d("VALUE", counter.toString())
        contadorNum.text = "El contador es: " + counter
    }

    fun incrementCounter() {
        counter++
        updateCounter()
        sendMessageToMobile()
    }

    private fun sendMessageToMobile() {
        val putDataMapRequest = PutDataMapRequest.create("/counter_wear")
        putDataMapRequest.dataMap.putInt("counter_value", counter)
        val dataItemTask = dataClient.putDataItem(putDataMapRequest.asPutDataRequest())
        dataItemTask.addOnSuccessListener {
            Log.i("DIEGO", "DATA ENVIADA")
        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }
}


@Composable
fun WearApp(greetingName: String) {
    DgalConnectionTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}
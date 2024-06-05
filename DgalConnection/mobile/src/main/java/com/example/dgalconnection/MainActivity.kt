package com.example.dgalconnection

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    companion object {
        private var instance: MainActivity? = null

        fun getInstance(): MainActivity? {
            return instance
        }
    }

    private lateinit var dataClient: DataClient
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        dataClient = Wearable.getDataClient(this)
        val btnAumentar = findViewById<Button>(R.id.btn_Aumentar)
        val conection = findViewById<TextView>(R.id.conection)
        btnAumentar.setOnClickListener{
            incrementCounter()
        }
    }

    fun updateCounter(value: Int) {
        counter = value
        val contadorText = findViewById<TextView>(R.id.contador)
        contadorText.text = "El contador es: " + counter
    }

    fun incrementCounter() {
        counter++
        val contadorText = findViewById<TextView>(R.id.contador)
        contadorText.text = "El contador es: $counter"
        sendDataToWearable()
    }

    private fun sendDataToWearable() {
        val putDataMapRequest = PutDataMapRequest.create("/counter")
        putDataMapRequest.dataMap.putInt("counter_value", counter)
        val dataItemTask = dataClient.putDataItem(putDataMapRequest.asPutDataRequest())
        dataItemTask.addOnSuccessListener {
            Log.i("DIEGO", "DATA ENVIADA")
        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }

}
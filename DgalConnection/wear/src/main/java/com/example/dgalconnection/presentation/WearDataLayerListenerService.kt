package com.example.dgalconnection.presentation

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService

class WearDataLayerListenerService : WearableListenerService() {

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)

        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/counter") {
                val dataMapItem = DataMapItem.fromDataItem(event.dataItem)
                val counterValue = dataMapItem.dataMap.getInt("counter_value")
                Log.d("DIEGO", "Received counter value: $counterValue")

                val mainActivity = MainActivity.getInstance()
                mainActivity?.let {
                    it.counter = counterValue
                    it.updateCounter()
                }

                getConnectedNode()
            }
        }
    }

    private fun getConnectedNode() {
        val nodesTask = Wearable.getNodeClient(this).connectedNodes
        nodesTask.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val connectedNodes = task.result
                if (connectedNodes.isNotEmpty()) {
                    val connectedNode = connectedNodes[0] // Obtener el primer nodo conectado
                    Log.d("DIEGO", "Connected node: ${connectedNode.displayName}")
                } else {
                    Log.d("DIEGO", "No connected nodes")
                }
            } else {
                Log.e("DIEGO", "Failed to get connected nodes", task.exception)
            }
        })
    }
}
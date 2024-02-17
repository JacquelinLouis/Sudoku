package com.example.sudoku.repository.inspect

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.example.sudoku.repository.GridRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class InspectorBroadcastReceiver(
    private val context: Context,
    private val gridRepository: GridRepository
): BroadcastReceiver(), AutoCloseable {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        Log.d(this@InspectorBroadcastReceiver::class.simpleName, "Register")
        context.registerReceiver(
            this,
            IntentFilter(SUDOKU),
            Context.RECEIVER_EXPORTED
        )
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d(this@InspectorBroadcastReceiver::class.simpleName, "Received")
        coroutineScope.launch {
            val dump = p1?.getStringExtra(DUMP)
            if (dump == GRIDS)
                gridRepository.getGridsMetadata().first().onEach { gridMetadataEntity ->
                    Log.d(this@InspectorBroadcastReceiver::class.simpleName, gridMetadataEntity.toString())
                    val grid = gridRepository.getGridData(gridMetadataEntity.gridMetadataId)
                    Log.d(this@InspectorBroadcastReceiver::class.simpleName, grid.first().values)
                }
        }
    }

    override fun close() {
        Log.d(this@InspectorBroadcastReceiver::class.simpleName, "Unregister")
        context.unregisterReceiver(this)
        coroutineScope.cancel()
    }

    companion object {
        private const val GRIDS = "grids"
        private const val DUMP = "dump"
        private const val SUDOKU = "sudoku"
    }

}
package com.example.atlantapp.socket

import com.example.atlantapp.common.AppCoroutineScope
import com.example.atlantapp.socket.model.Options
import com.example.atlantapp.socket.model.SocketRequest
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import timber.log.Timber
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.properties.Delegates

private const val DELAY_MS = 5000L

class SocketUpdatesManager private constructor(
    appScope: AppCoroutineScope,
    private val options: Options,
    private val client: OkHttpClient,
    private val gson: Gson,
    private val updateHandlers: List<UpdateHandler>,
    private val initDispatcher: CoroutineDispatcher
) : CoroutineScope by (appScope + initDispatcher), UpdatesManager {

    constructor(
        appScope: AppCoroutineScope,
        options: Options,
        okHttpClient: OkHttpClient,
        gson: Gson,
        updateHandlers: List<UpdateHandler>
    ) : this(
        appScope,
        options,
        okHttpClient,
        gson,
        updateHandlers,
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    )

    @Volatile
    private var socket: WebSocket? = null

    private val isStarted = AtomicBoolean(false)

    private var state by Delegates.observable(UpdatesState.DISCONNECTED) { _, _, newValue ->
        when (newValue) {
            UpdatesState.CONNECTED -> {
                ping()
                subscribeToTransactions()
            }
            else -> {
                // do nothing
            }
        }
    }

    override fun start() {
        launch { startActual() }
    }

    override fun stop() {
        launch(NonCancellable) { stopActual() }
    }

    private suspend fun startActual() {
        isStarted.set(true)
        when (state) {
            UpdatesState.DISCONNECTED,
            UpdatesState.INITIALIZATION_FAILED,
            UpdatesState.CONNECTING_FAILED ->
                connectSocket()
            UpdatesState.INITIALIZING ->
                Timber.tag("SOCKET").d("Trying to start update manager while it is initializing")
            UpdatesState.CONNECTING ->
                Timber.tag("SOCKET").d("Trying to start update manager while it is connecting")
            UpdatesState.CONNECTED ->
                Timber.tag("SOCKET").d("Trying to start update manager while it is connected")
        }
    }

    private fun stopActual() {
        if (isStarted.compareAndSet(true, false)) {
            Timber.tag("SOCKET").d("Stopping update manager")
            unsubscribeAll()
            disconnectSocket()
            return
        }

        Timber.tag("SOCKET").w("Trying to stop update manager while it is already stopped")
    }

    private suspend fun connectSocket() {
        if (state == UpdatesState.CONNECTED) {
            Timber.tag("SOCKET").d("Trying to connect update manager while it is already connected")
            return
        }

        state = UpdatesState.INITIALIZING

        try {
            Timber.tag("SOCKET").d("Connecting: initializing update handlers")
            updateHandlers.forEach { it.initialize() }
        } catch (e: Throwable) {
            state = UpdatesState.INITIALIZATION_FAILED
            return
        }

        state = UpdatesState.CONNECTING
        Timber.tag("SOCKET")
            .d("Connecting: Update handlers are initialized, connecting to event stream")

        state = UpdatesState.CONNECTED
        try {
            socket = client.newWebSocket(
                options.toRequest(),
                OkHttpWebSocketListener()
            )
        } catch (e: Throwable) {
            state = UpdatesState.CONNECTING_FAILED
        }
    }

    private fun disconnectSocket() {
        socket?.cancel()
        state = UpdatesState.DISCONNECTED
        Timber.tag("SOCKET").d("Disconnected from event source")
    }

    private fun ping() {
        val request = gson.toJson(SocketRequest("ping"))
        socket?.send(request)
    }

    private fun subscribeToTransactions() {
        val request = SocketRequest("unconfirmed_sub")
        socket?.send(gson.toJson(request))
    }

    private fun unsubscribeAll() {
        val request = SocketRequest("unconfirmed_unsub")
        socket?.send(gson.toJson(request))
    }

    private suspend fun onUpdate(data: String) {
        val json = JSONObject(data)
        when (json.optString("op")) {
            UpdateType.PING.stringValue ->
                Timber.tag("SOCKET").d("PING")
            UpdateType.TRANSACTION.stringValue -> {
                Timber.tag("SOCKET").d("Event received, data = $data")
                updateHandlers.forEach {
                    it.onUpdate(UpdateType.TRANSACTION, json)
                }
            }
            else ->
                Timber.tag("SOCKET").d("Unknown event received, data = $data")
        }
    }

    private suspend fun restartInternal() {
        if (!isStarted.get()) {
            Timber.tag("SOCKET").d("Stopped")
        } else {
            Timber.tag("SOCKET").d("Restarting")
            disconnectSocket()
            delay(DELAY_MS)

            // Check again since stop may has been called while waiting for delay.
            if (isStarted.get()) {
                connectSocket()
            }
        }
    }

    private inner class OkHttpWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            Timber.tag("SOCKET").d("Event stream is connected")
            launch { state = UpdatesState.CONNECTED }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Timber.tag("SOCKET").d("Event stream is closed")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            launch(Dispatchers.Default) { onUpdate(text) }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Timber.tag("SOCKET").d("Event stream is failed ${t.message}")
            if (state == UpdatesState.CONNECTING) {
                launch { state = UpdatesState.CONNECTING_FAILED }
            } else {
                launch { restartInternal() }
            }
        }
    }
}

private fun Options.toRequest() =
    Request.Builder()
        .url(url)
        .addHeader("Origin", origin)
        .build()
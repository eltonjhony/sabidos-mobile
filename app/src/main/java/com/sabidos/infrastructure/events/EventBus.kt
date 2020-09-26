package com.sabidos.infrastructure.events

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.filter
import kotlinx.coroutines.channels.map
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

object EventBus {
    val bus = ConflatedBroadcastChannel<Any>(object {})

    fun send(o: Any) {
        GlobalScope.launch {
            bus.send(o)
        }
    }

    inline fun <reified T> on() = bus.asFlow().drop(1).filter { it is T }.map { it as T }

    inline fun <reified T> asChannel(): ReceiveChannel<T> {
        return bus.openSubscription().filter { it is T }.map { it as T }
    }

}
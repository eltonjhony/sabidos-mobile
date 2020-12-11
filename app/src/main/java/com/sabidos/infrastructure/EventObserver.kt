package com.sabidos.infrastructure

import androidx.lifecycle.Observer

class EventObserver<T>(private val onEventUnhandledContent: (Resource<T>) -> Unit) :
    Observer<Resource<T>> {
    override fun onChanged(event: Resource<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent.invoke(it)
        }
    }
}
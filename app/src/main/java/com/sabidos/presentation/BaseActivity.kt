package com.sabidos.presentation

import androidx.appcompat.app.AppCompatActivity
import com.sabidos.presentation.components.AppLoadingComponent

open class BaseActivity: AppCompatActivity() {
    val loading: AppLoadingComponent by lazy { AppLoadingComponent() }
}
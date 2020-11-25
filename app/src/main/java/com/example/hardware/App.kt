package com.example.hardware

import android.app.Application
import android.content.Context
import com.example.hardware.dagger.AppComponent
import com.example.hardware.dagger.AppModule
import com.example.hardware.dagger.DaggerAppComponent

class App(context: Context) : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(context))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }
}
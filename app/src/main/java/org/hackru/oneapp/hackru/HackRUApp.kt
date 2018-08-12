package org.hackru.oneapp.hackru

import android.app.Application
import org.hackru.oneapp.hackru.di.AppComponent
import org.hackru.oneapp.hackru.di.AppModule
import org.hackru.oneapp.hackru.di.DaggerAppComponent

class HackRUApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}
package com.qpeterp.todolock.application

import android.app.Application

class App: Application() {
    companion object {
        lateinit var sharedPreference: SharedPreference
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreference = SharedPreference(applicationContext)
    }
}
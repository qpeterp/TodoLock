package com.qpeterp.todolock.application

import android.content.Context

class SharedPreference(context: Context) {
    private val sharedPreference = 
   	context.getSharedPreferences("lockState", Context.MODE_PRIVATE)

    var name: String?
        get() = sharedPreference.getString("lockState", "")
        set(value) = sharedPreference.edit().putString("lockState", value).apply()
}
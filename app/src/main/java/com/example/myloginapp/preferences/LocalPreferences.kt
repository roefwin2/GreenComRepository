package com.example.myloginapp.preferences

import android.content.Context
import android.content.SharedPreferences


const val APP_PREF_INT_EXAMPLE = "myLocal"
class Prefs (context: Context)
{
    private val preferences: SharedPreferences = context.getSharedPreferences("local",Context.MODE_PRIVATE)

    var intExamplePref: String?
        get() = preferences.getString(APP_PREF_INT_EXAMPLE, "fr")
        set(value) = preferences.edit().putString(APP_PREF_INT_EXAMPLE, value).apply()
}
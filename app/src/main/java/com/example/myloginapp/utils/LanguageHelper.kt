package com.example.myloginapp.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.core.os.ConfigurationCompat
import java.util.*

/**
 * Helper for use the local from : https://medium.com/swlh/the-all-in-one-guide-for-changing-app-locale-dynamically-in-android-kotlin-d2506e5535d0
 * https://stackoverflow.com/questions/65662126/kotlin-how-to-change-the-locale
 */

class LocaleUtil  {
    companion object {

        fun getLocalizedConfiguration(locale: Locale): Configuration {
            val config = Configuration()
            return config.apply {
                config.setLayoutDirection(locale)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    config.setLocale(locale)
                    val localeList = LocaleList(locale)
                    LocaleList.setDefault(localeList)
                    config.setLocales(localeList)
                } else {
                    config.setLocale(locale)
                }
            }
        }



        fun setLocale(context: Context, language: String): Context {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return updateResources(context, language)
            }

            return updateResourcesLegacy(context, language)

        }

        @TargetApi(Build.VERSION_CODES.N)
        private fun updateResources(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)

            val configuration = context.resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)

            return context.createConfigurationContext(configuration)
        }

        @Suppress("DEPRECATION")
        private fun updateResourcesLegacy(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)

            val resources = context.resources

            val configuration = resources.configuration
            configuration.locale = locale
            configuration.setLayoutDirection(locale)

            resources.updateConfiguration(configuration, resources.displayMetrics)

            return context
        }

    }
}
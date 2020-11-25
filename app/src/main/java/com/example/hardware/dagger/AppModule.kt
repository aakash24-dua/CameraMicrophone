package com.example.hardware.dagger

import android.content.Context
import com.example.hardware.util.SecureItPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideSecureItPreferences(): SecureItPreferences {
        return SecureItPreferences(context)
    }

}
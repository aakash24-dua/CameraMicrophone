package com.example.hardware.dagger

import com.example.hardware.App
import com.example.hardware.ui.main.view.StartActivity
import com.example.hardware.ui.main.view.MicrophoneFragment
import com.example.hardware.util.SecureItPreferences
import com.example.hardware.ui.main.viewmodel.MicrophoneViewmodel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(app: App)


    fun inject(fragment: MicrophoneFragment)
    fun inject(activity: StartActivity)

    fun secureItPreferences(): SecureItPreferences
    fun inject(viewmodel: MicrophoneViewmodel)

}
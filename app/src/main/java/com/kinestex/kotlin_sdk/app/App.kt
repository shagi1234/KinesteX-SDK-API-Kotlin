package com.kinestex.kotlin_sdk.app

import android.app.Application
import com.kinestex.kinestexsdkkotlin.secure_api.KinesteXSDKAPI

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KinesteXSDKAPI.createAndInitialize(this)
    }
}
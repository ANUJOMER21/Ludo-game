package com.royalkingmatka.ludogame

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
       /* Log.d("FirebaseInitialization", "Attempting to initialize Firebase...")
        try {
            FirebaseApp.initializeApp(this)
            Log.d("FirebaseInitialization", "Firebase initialized successfully.")
        } catch (e: Exception) {
            Log.e("FirebaseInitialization", "Firebase initialization failed.", e)
        }*/
    }
}

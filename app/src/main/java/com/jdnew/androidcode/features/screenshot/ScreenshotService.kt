package com.jdnew.androidcode.features.screenshot

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

const val SCREEN_CAPTURE_CHANNEL_ID = "Screen Capture ID"
const val SCREEN_CAPTURE_CHANNEL_NAME = "Screen Capture"

class ScreenshotService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(1, NotificationCompat.Builder(this, SCREEN_CAPTURE_CHANNEL_ID).build())
    }
}
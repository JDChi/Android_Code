package com.jdnew.androidcode.features.screenshot

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jdnew.androidcode.util.Shooter


class ScreenshotActivity : ComponentActivity() {
    var mediaProjectionIntent: Intent? = null
    var mediaProjectionResultCode = Activity.RESULT_CANCELED

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ScreenshotActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createMediaProjectionNotificationChannel()
        startMediaProjectionForegroundService()
        setContent {
            takeAScreenshotUI()
            loadingUI()
        }
    }



    private fun createMediaProjectionNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val screenCaptureChannel = NotificationChannel(
            SCREEN_CAPTURE_CHANNEL_ID,
            SCREEN_CAPTURE_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(screenCaptureChannel)
    }

    private fun startMediaProjectionForegroundService() {
        startForegroundService(Intent(this, ScreenshotService::class.java))
    }

    @Preview
    @Composable
    private fun takeAScreenshotUI() {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                takeAScreenshot()
            }) {
                Text(text = "start take a screenshot")
            }
        }
    }

    @Composable
    private fun loadingUI() {
        CircularProgressIndicator()
    }

    private fun takeAScreenshot() {
        if (mediaProjectionIntent != null && mediaProjectionResultCode == Activity.RESULT_OK) {
            startScreenshot(mediaProjectionIntent!!, mediaProjectionResultCode)
        } else {
            val mediaProjectionIntent =
                (getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager).createScreenCaptureIntent()
            mediaProjectionForResult.launch(mediaProjectionIntent)
        }

    }

    private fun startScreenshot(intent: Intent, resultCode: Int) {
        val shooter = Shooter(this@ScreenshotActivity, resultCode, intent)
        shooter.startScreenShot("", object : Shooter.OnShotListener {
            override fun onFinish(path: String?) {
                Toast.makeText(this@ScreenshotActivity, "succeed", Toast.LENGTH_SHORT).show()
            }

            override fun onError() {
                Toast.makeText(this@ScreenshotActivity, "error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    // activityForResult 被废弃了，新的应该用这个
    private var mediaProjectionForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                mediaProjectionIntent = result.data
                mediaProjectionResultCode = result.resultCode
                startScreenshot(mediaProjectionIntent!!, mediaProjectionResultCode)
            }
        }


}
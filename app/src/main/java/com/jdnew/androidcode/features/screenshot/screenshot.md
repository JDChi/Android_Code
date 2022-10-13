# 截屏

- 需要先开启前台服务

```xml

<manifest>
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <application>
        <service android:name=".features.screenshot.ScreenshotService"
            android:foregroundServiceType="mediaProjection" />
    </application>
</manifest>
```

```kotlin
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

```

- 获取 Media Projection 数据

```kotlin
  val mediaProjectionIntent =
    (getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager).createScreenCaptureIntent()
mediaProjectionForResult.launch(mediaProjectionIntent)

private var mediaProjectionForResult =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            mediaProjectionIntent = result.data
            mediaProjectionResultCode = result.resultCode
            startScreenshot(mediaProjectionIntent!!, mediaProjectionResultCode)
        }
    }
```

- 使用封装好的 Shooter 进行截屏
```kotlin
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
```
## 参考

- [外挂三部曲（二） —— Android 应用外截屏](https://juejin.cn/post/7113203120229842981)
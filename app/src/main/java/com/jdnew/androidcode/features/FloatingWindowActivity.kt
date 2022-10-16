package com.jdnew.androidcode.features

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.jdnew.androidcode.R
import com.jdnew.androidcode.features.screenshot.ScreenshotActivity
import com.jdnew.androidcode.util.DeviceUtil

class FloatingWindowActivity : AppCompatActivity() {

    private val TAG = this@FloatingWindowActivity.toString()

    private lateinit var btnStart: AppCompatButton
    private lateinit var ivIcon: AppCompatImageView
    private lateinit var wm: WindowManager
    private lateinit var wlp: WindowManager.LayoutParams
    private lateinit var iconGestureDetector: GestureDetector

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, FloatingWindowActivity::class.java)
        }
    }

    // 将手势事件交由 GestureDetector 来处理
    @SuppressLint("ClickableViewAccessibility")
    private val touchListener: View.OnTouchListener =
        View.OnTouchListener(fun(_: View, event: MotionEvent): Boolean {
            iconGestureDetector.onTouchEvent(event)
            return super.onTouchEvent(event)
        })

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floating_window)
        btnStart = findViewById(R.id.bt_start)
        ivIcon = AppCompatImageView(this)
        ivIcon.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                com.google.android.material.R.drawable.ic_clock_black_24dp
            )
        )
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        wlp = WindowManager.LayoutParams().apply {
            width = WRAP_CONTENT
            height = WRAP_CONTENT
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            format = PixelFormat.RGBA_8888
            flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            x = 0
            y = 0
        }

        var downX = 0
        var downY = 0
        iconGestureDetector =
            GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent): Boolean {
                    downX = e.rawX.toInt()
                    downY = e.rawY.toInt()
                    return super.onDown(e)
                }

                override fun onScroll(
                    e1: MotionEvent,
                    e2: MotionEvent,
                    distanceX: Float,
                    distanceY: Float
                ): Boolean {
                    val moveX = e2.rawX.toInt()
                    val moveY = e2.rawY.toInt()
                    val dx = moveX - downX
                    val dy = moveY - downY
                    downX = moveX
                    downY = moveY

                    wlp.apply {
                        x += dx
                        y += dy
                    }
                    wm.updateViewLayout(ivIcon, wlp)
                    return super.onScroll(e1, e2, distanceX, distanceY)
                }
            })
        ivIcon.setOnTouchListener(touchListener)

        btnStart.setOnClickListener {
            showFloatingWindow()
        }
    }

    var isAddView: Boolean = false
    private fun showFloatingWindow() {
        DeviceUtil.checkSuspendedWindowPermission(this) {
            if (!isAddView) {
                wm.addView(ivIcon, wlp)
                isAddView = true
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (isAddView) {
            wm.removeViewImmediate(ivIcon)
        }
    }
}
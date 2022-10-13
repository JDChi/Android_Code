package com.jdnew.androidcode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.jdnew.androidcode.features.RevealAnimationActivity
import com.jdnew.androidcode.features.screenshot.ScreenshotActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnRevealAnimation: AppCompatButton
    private lateinit var btnScreenshot: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRevealAnimation = findViewById(R.id.bt_reveal_animation)
        btnScreenshot = findViewById(R.id.bt_screenshot)
        btnRevealAnimation.setOnClickListener(this)
        btnScreenshot.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bt_reveal_animation -> startActivity(RevealAnimationActivity.newIntent(this))
            R.id.bt_screenshot -> startActivity(ScreenshotActivity.newIntent(this))
        }
    }
}
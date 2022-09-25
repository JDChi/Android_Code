package com.jdnew.androidcode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.jdnew.androidcode.features.RevealAnimationActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnRevealAnimation: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRevealAnimation = findViewById(R.id.bt_reveal_animation)
        btnRevealAnimation.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bt_reveal_animation -> startActivity(RevealAnimationActivity.newIntent(this))
        }
    }
}
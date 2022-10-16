package com.jdnew.androidcode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.widget.AppCompatButton
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.jdnew.androidcode.features.FloatingWindowActivity
import com.jdnew.androidcode.features.RevealAnimationActivity
import com.jdnew.androidcode.features.screenshot.ScreenshotActivity
import java.lang.reflect.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            buttons()
        }
    }

    @Composable
    private fun buttons() {
        LazyColumn(
            content = {
                item {
                    Button(onClick = { startActivity(RevealAnimationActivity.newIntent(this@MainActivity)) }) {
                        Text(text = "Reveal Animation")
                    }
                }
                item {
                    Button(onClick = { startActivity(ScreenshotActivity.newIntent(this@MainActivity)) }) {
                        Text(text = "Screenshot")
                    }
                }
                item {
                    Button(onClick = { startActivity(FloatingWindowActivity.newIntent(this@MainActivity)) }) {
                        Text(text = "Floating Window")
                    }
                }
            },
        )
    }
}
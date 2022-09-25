package com.jdnew.androidcode.features

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.jdnew.androidcode.R

/**
 * [Create a circular reveal animation](https://developer.android.com/develop/ui/views/animations/reveal-or-hide-view#Reveal)
 */
class RevealAnimationActivity : AppCompatActivity() {
    private lateinit var btnStart: AppCompatButton
    private lateinit var viewTest: View

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RevealAnimationActivity::class.java)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reveal_animation)
        btnStart = findViewById(R.id.bt_start)
        viewTest = findViewById(R.id.view_test)
        btnStart.setOnClickListener {
            if (viewTest.visibility == View.VISIBLE) {
                startHideAnimation()
            } else {
                startShowAnimation()
            }

        }

    }


    private fun startHideAnimation() {
        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            val cx = viewTest.x.toInt()
            val cy = viewTest.height

            // get the initial radius for the clipping circle
            val initialRadius = Math.hypot(
                (viewTest.x + viewTest.width).toDouble(),
                (viewTest.y + viewTest.height).toDouble()
            ).toFloat()

            // create the animation (the final radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(viewTest, cx, cy, initialRadius, 0f)

            // make the view invisible when the animation is done
            anim.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    viewTest.visibility = View.GONE
                }
            })

            // start the animation
            anim.start()
        } else {
            // set the view to visible without a circular reveal animation below Lollipop
            viewTest.visibility = View.VISIBLE
        }
    }


    private fun startShowAnimation() {
        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            // 圆心为 view 的中心
//            val cx = viewTest.width / 2
//            val cy = viewTest.height / 2
            // 圆心为 view 的左上角
//            val cx = viewTest.x.toInt()
//            val cy = viewTest.y.toInt()
            // 圆心为 view 的右下角
//            val cx = viewTest.width
//            val cy = viewTest.height
            val cx = viewTest.x.toInt()
            val cy = viewTest.height

            // get the final radius for the clipping circle
//            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
            // 使用 hypot 算出对角线，即半径
            val finalRadius = Math.hypot(
                (viewTest.x + viewTest.width).toDouble(),
                (viewTest.y + viewTest.height).toDouble()
            ).toFloat()

            // create the animator for this view (the start radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(viewTest, cx, cy, 0f, finalRadius)
            // make the view visible and start the animation
            viewTest.visibility = View.VISIBLE
            anim.start()
        } else {
            // set the view to invisible without a circular reveal animation below Lollipop
            viewTest.visibility = View.INVISIBLE
        }
    }
}
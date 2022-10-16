package com.jdnew.androidcode.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast

object DeviceUtil {
    private const val REQUEST_FLOAT_CODE = 1001

    /**
     * 判断悬浮窗权限权限
     */
    private fun hasOverlaysPermission(context: Context?): Boolean {
        var result = true
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                val clazz: Class<*> = Settings::class.java
                val canDrawOverlays =
                    clazz.getDeclaredMethod("canDrawOverlays", Context::class.java)
                result = canDrawOverlays.invoke(null, context) as Boolean
            } catch (e: Exception) {
                Log.e("ServiceUtils", Log.getStackTraceString(e))
            }
        }
        return result
    }

    fun checkSuspendedWindowPermission(context: Activity, block: () -> Unit) {
        if (hasOverlaysPermission(context)) {
            block()
        } else {
            Toast.makeText(context, "请开启悬浮窗权限", Toast.LENGTH_SHORT).show()
            context.startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = Uri.parse("package:${context.packageName}")
            }, REQUEST_FLOAT_CODE)
        }
    }

}
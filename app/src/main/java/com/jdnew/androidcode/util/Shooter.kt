package com.jdnew.androidcode.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.Image.Plane
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.SystemClock
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import kotlinx.coroutines.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.SoftReference
import java.nio.ByteBuffer


/**
 * Created by wei on 16-12-1.
 *
 *
 * Remind:
 * Run this class after you got record permission.
 */
@SuppressLint("WrongConstant")
class Shooter(context: Context, reqCode: Int, data: Intent?) {
    private val mRefContext: SoftReference<Context>
    private var mImageReader: ImageReader? = null
    private var mMediaProjection: MediaProjection? = null
    private var mVirtualDisplay: VirtualDisplay? = null
    private var mLocalUrl: String = ""
    private var mOnShotListener: OnShotListener? = null
    private var mHeight: Int = 0
    private var mWidth: Int = 0

    //using a default path.
    private val savedPath: String
        get() {
            if (TextUtils.isEmpty(mLocalUrl)) {
                mLocalUrl =
                    (context!!.getExternalFilesDir("screenshot")!!.absoluteFile.toString() + "/"
                            + SystemClock.currentThreadTimeMillis() + ".png")
            }
            return mLocalUrl
        }

    init {
        mRefContext = SoftReference(context)
        mMediaProjection = mediaProjectionManager.getMediaProjection(reqCode, (data)!!)
        val window: WindowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val mDisplay: Display = window.defaultDisplay
        val metrics = DisplayMetrics()
        mDisplay.getRealMetrics(metrics)
        mWidth = metrics.widthPixels //size.x;
        mHeight = metrics.heightPixels //size.y;
        mImageReader = ImageReader.newInstance(
            mWidth,
            mHeight,
            PixelFormat.RGBA_8888,  //this is necessary to equal buffer format in #copyPixelsFromBuffer.
            1
        )
    }

    private fun virtualDisplay() {
        mVirtualDisplay = mMediaProjection!!.createVirtualDisplay(
            "screen-mirror",
            mWidth,
            mHeight,
            Resources.getSystem().displayMetrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mImageReader!!.surface, null, null
        )
    }

    /**
     * @param onShotListener
     * @param savedPath
     */
    fun startScreenShot(savedPath: String, onShotListener: OnShotListener?) {
        mLocalUrl = savedPath
        startScreenShot(onShotListener)
    }

    /**
     * This method will using [.getSavedPath] to save.
     * @param onShotListener
     */
    private fun startScreenShot(onShotListener: OnShotListener?) {
        hasPermission = true
        mOnShotListener = onShotListener
        virtualDisplay()
        GlobalScope.launch (Dispatchers.Main){
            delay(800)
            val image: Image = mImageReader!!.acquireLatestImage()
            val isSuccess = saveImageToFile(image)
            if (isSuccess) {
                mOnShotListener?.onFinish(savedPath)
            } else {
                mOnShotListener?.onError()
            }
        }
    }

    private suspend fun saveImageToFile(image: Image): Boolean {
        val result = withContext(Dispatchers.IO) {
            val width: Int = image.width
            val height: Int = image.height
            val planes: Array<Plane> = image.planes
            val buffer: ByteBuffer = planes.get(0).buffer
            //每个像素的间距
            val pixelStride: Int = planes.get(0).pixelStride
            //总的间距
            val rowStride: Int = planes.get(0).rowStride
            val rowPadding: Int = rowStride - pixelStride * width
            var bitmap: Bitmap? = Bitmap.createBitmap(
                width + rowPadding / pixelStride, height,
                Bitmap.Config.ARGB_8888
            ) //even though ARGB8888 will consume more memory,it has better compatibility on device.
            bitmap!!.copyPixelsFromBuffer(buffer)
            bitmap = Bitmap.createBitmap((bitmap), 0, 0, width, height)
            image.close()
            var fileImage: File?
            if (bitmap != null) {
                try {
                    fileImage = File(savedPath)
                    if (!fileImage.exists()) {
                        fileImage.createNewFile()
                    }
                    val out = FileOutputStream(fileImage)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
                    out.flush()
                    out.close()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    release()
                    return@withContext false
                } catch (e: IOException) {
                    e.printStackTrace()
                    release()
                    return@withContext false
                }
            }
            if (bitmap != null && !bitmap.isRecycled) {
                bitmap.recycle()
            }
            release()
            return@withContext true
        }
        return result
    }

    private fun release() {
        mVirtualDisplay?.release()
        mMediaProjection?.stop()
    }

    private val mediaProjectionManager: MediaProjectionManager
        get() = context!!.getSystemService(
            Context.MEDIA_PROJECTION_SERVICE
        ) as MediaProjectionManager
    private val context: Context?
        get() {
            return mRefContext.get()
        }

    interface OnShotListener {
        fun onFinish(path: String?)
        fun onError()
    }

    companion object {
        var hasPermission: Boolean = false
    }
}
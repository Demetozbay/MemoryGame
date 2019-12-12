package com.r00t.demetapp.commons

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.io.InputStream

class DownloadImageTask constructor() : AsyncTask<String, Void, Bitmap>() {
    var imageView:ImageView? = null

    constructor(bmImage:ImageView) : this() {
        this.imageView=bmImage
    }

    override fun doInBackground(vararg p0: String?): Bitmap {
        var urlDisplay=p0[0]
        var bitmap:Bitmap? = null
        try {
            var inputStream:InputStream = java.net.URL(urlDisplay).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (ex:Exception) {
            Log.e("Error",ex.message)
            ex.printStackTrace()
        }
        return bitmap!!
    }

    override fun onPostExecute(result: Bitmap?) {
        imageView?.setImageBitmap(result)
    }
}
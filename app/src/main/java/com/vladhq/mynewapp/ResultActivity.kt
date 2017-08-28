package com.vladhq.mynewapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import android.content.Intent
import android.net.Uri
import android.text.ClipboardManager
import android.view.View
import android.widget.Toast


class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val s = intent.getStringExtra("URL")
        val textView = findViewById(R.id.url_text) as TextView
        textView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView.text = s
        val editor = getSharedPreferences("notes", Context.MODE_PRIVATE).edit()
        editor.putString("draft", "")
        editor.apply()


        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(s, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0..width - 1) {
                for (y in 0..height - 1) {
                    bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }
            (findViewById(R.id.qrCode) as ImageView).setImageBitmap(bmp)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
    fun clickLink(view: View) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra("URL")))
        startActivity(browserIntent)
    }

    fun copyLink(view: View) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setText(intent.getStringExtra("URL"));
        val toast = Toast.makeText(getBaseContext(), "Link successfully copied: "+ intent.getStringExtra("URL"), Toast.LENGTH_LONG);
        toast.show();
    }



}



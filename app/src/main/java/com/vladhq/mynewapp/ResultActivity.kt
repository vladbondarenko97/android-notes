package com.vladhq.mynewapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.ClipboardManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter


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

    fun shareLink() {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = intent.getStringExtra("URL")
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "View this paste at ")
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.result, menu)
        return true
    }

    fun new_paste(){
        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_share ->
                shareLink()
            R.id.action_new ->
                new_paste()
            R.id.action_exit ->
                System.exit(0)
        }
        return super.onOptionsItemSelected(item)
    }

    fun copyLink(view: View) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setText(intent.getStringExtra("URL"));
        val toast = Toast.makeText(getBaseContext(), "Link successfully copied: "+ intent.getStringExtra("URL"), Toast.LENGTH_LONG);
        toast.show();
    }



}



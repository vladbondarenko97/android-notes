package com.vladhq.mynewapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import java.nio.charset.Charset
import android.content.SharedPreferences




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val notes = findViewById(R.id.text_note) as EditText

        val prefs = getSharedPreferences("notes", Context.MODE_PRIVATE)
        val restoredText = prefs.getString("draft", null)
        if (restoredText != null) {
            val name = prefs.getString("draft", "")
            notes.setText(name)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_exit ->
                System.exit(0)
        }
        return super.onOptionsItemSelected(item)
    }

    fun exit_app(view: View) {
        val notes = findViewById(R.id.text_note) as EditText
        val editor = getSharedPreferences("notes", Context.MODE_PRIVATE).edit()
        editor.putString("draft", notes.text.toString())
        editor.apply()
        val toast = Toast.makeText(getBaseContext(), "Draft saved.", Toast.LENGTH_LONG);
        toast.show();
        System.exit(0)
    }

    fun saveNote(view: View) {
        val notes = findViewById(R.id.text_note) as EditText

        if (notes.text.toString().matches("".toRegex())) {
            Toast.makeText(this, "You cannot save an empty paste!", Toast.LENGTH_SHORT).show()
            notes.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(notes, InputMethodManager.SHOW_IMPLICIT)
            return
        }

        val client = AsyncHttpClient()
        val progress = ProgressDialog(this)
        println("TESTING: "+notes.text.toString());
        val params = RequestParams("data", notes.text.toString())
        client.post("http://vlad.tk/p/generate.php", params, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                if (responseBody != null) {
                    val handler = Handler()
                    handler.postDelayed(Runnable {
                        println("SUCCESS: Note uploaded at "+responseBody.toString(Charset.defaultCharset()))
                        progress.dismiss();
                        val intent = Intent(baseContext, ResultActivity::class.java)
                        intent.putExtra("URL", responseBody.toString(Charset.defaultCharset()))
                        intent.putExtra("NOTE", notes.text.toString())
                        startActivity(intent)
                    }, 500)
                }
            }

            override fun onStart() {
                progress.setTitle("Saving note")
                progress.setMessage("Wait while your paste is saved & uploaded...")
                progress.setCancelable(false)
                progress.show()
            }


            override fun onFailure(statusCode: Int, headers: Array<Header>, errorResponse: ByteArray, e: Throwable) {
                Tools.exceptionToast(getApplicationContext(), "Error uploading  your paste. Please try again later.");
            }

            override fun onRetry(retryNo: Int) {
                // called when request is retried
            }
        })
    }

    object Tools {
        fun exceptionToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}
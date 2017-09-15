package com.vladhq.mynewapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.ClipboardManager
import android.text.format.DateFormat
import android.view.View
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import java.util.*


class ContentActivity : AppCompatActivity() {

    private val pasteList = ArrayList<Paste>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: PastesAdapter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView

        registerForContextMenu(recyclerView);
        mAdapter = PastesAdapter(pasteList)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter

        preparePasteData()

    }

    fun gotoBeginning(view: View) {
        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
    }

    fun share(view: View) {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = findViewById(R.id.url) as TextView
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "View this paste at ")
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody.getText())
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    fun copy(view: View) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val url = findViewById(R.id.url) as TextView
        clipboard.setText(url.getText());
        val toast = Toast.makeText(getBaseContext(), "Link successfully copied: "+ url.getText(), Toast.LENGTH_LONG);
        toast.show();
    }
    private fun preparePasteData() {

        val prefs = getSharedPreferences("notes", Context.MODE_PRIVATE)
        val json = prefs.getString("list", null)
        var warning = findViewById(R.id.warning) as TextView

        if (json != null) {
            if(warning.getVisibility() == View.VISIBLE) {
                warning.setVisibility(View.INVISIBLE);
            }

            try {

                val obj = JSONObject(json)
                for (i in 0..obj.names().length() - 1) {
                    println("JSON TESTING: unixTime = " + obj.names().getString(i) + " url = " + obj.get(obj.names().getString(i)))
                    val unix_time = DateFormat.format("MM/dd/yyyy hh:mm:ss aa", obj.names().getString(i).toLong() * 1000);
                    var paste = Paste(unix_time.toString(), obj.get(obj.names().getString(i)).toString(), "N/A")
                    pasteList.add(paste)
                }


            } catch (t: Throwable) {
                println("Error loading JSON from shared preferences. (4)");
            }

            mAdapter!!.notifyDataSetChanged()
        } else {
            warning.setVisibility(View.VISIBLE);
        }

    }


}


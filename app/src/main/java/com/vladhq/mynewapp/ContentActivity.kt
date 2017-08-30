package com.vladhq.mynewapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View


class ContentActivity : AppCompatActivity() {

    private val pasteList = ArrayList<Paste>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: PastesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)


        recyclerView = findViewById(R.id.recycler_view) as RecyclerView

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
    private fun preparePasteData() {
        var paste = Paste("Title", "2015")
        pasteList.add(paste)

        var paste2 = Paste("Title 2", "2017")
        pasteList.add(paste2)


        mAdapter!!.notifyDataSetChanged()
    }


}

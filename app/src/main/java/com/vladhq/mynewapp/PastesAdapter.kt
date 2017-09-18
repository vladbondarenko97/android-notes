package com.vladhq.mynewapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnCreateContextMenuListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset

class PastesAdapter(private val pastesList: List<Paste>) : RecyclerView.Adapter<PastesAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), OnCreateContextMenuListener {
        var title: TextView
        var url: TextView
        var data: TextView

        init {
            title = view.findViewById<View>(R.id.title) as TextView
            url = view.findViewById<View>(R.id.url) as TextView
            data = view.findViewById<View>(R.id.data) as TextView
            view.setOnCreateContextMenuListener(this)

            view.setOnClickListener { v -> Toast.makeText(v.context, "Position is " + adapterPosition, Toast.LENGTH_SHORT).show() }

            view.setOnLongClickListener { false }
        }

        override fun onCreateContextMenu(menu: ContextMenu, view: View, contextMenuInfo: ContextMenu.ContextMenuInfo) {
            val prefs = view.context.getSharedPreferences("notes", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            val pastes = prefs.getString("list", null)

            menu.setHeaderTitle("Select The Action")
            menu.add("Rename").setOnMenuItemClickListener {
                try {

                    val obj = JSONObject(pastes)
                    try {
                        obj.remove(title.text.toString() + ":" + url.text.toString())
                        Toast.makeText(view.context, title.text.toString() + ":" + url.text.toString(), Toast.LENGTH_SHORT).show()
                        Toast.makeText(view.context, "RENAME " + obj.toString(), Toast.LENGTH_SHORT).show()

                    } catch (e: JSONException) {
                        println("Error renaming this object");
                    }

                    //editor.putString("list", obj.toString())
                    //println("Saved JSON to shared preferences - " + obj.toString())
                    //editor.apply()

                } catch (t: Throwable) {
                    println("Error loading JSON from shared preferences. (2)");
                }

                true
            }
            menu.add("Delete").setOnMenuItemClickListener {
                //Toast.makeText(view.context, "DELETE " + title.text, Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.paste_list_row, parent, false)

        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val paste = pastesList[position]
        holder.title.text = paste.title
        holder.url.text = paste.url
        holder.data.text = paste.data
    }

    override fun getItemCount(): Int {
        return pastesList.size
    }
}
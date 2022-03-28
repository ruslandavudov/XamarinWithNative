package com.example.nativelib.gui

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.nativelib.R
import com.example.nativelib.models.api.streaming.DataStreams


internal class StreamAdapter(
    private val context: Activity,
    private val dataSource: ArrayList<DataStreams>,
) : ArrayAdapter<DataStreams>(context, R.layout.listview_item, dataSource) {

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.listview_item, null)

        val label = view.findViewById<TextView>(R.id.label)!!
        label.text = "[id: ${dataSource[position].id}] ${dataSource[position].name}"

        return view
//        return super.getView(position, convertView, parent)
    }
}
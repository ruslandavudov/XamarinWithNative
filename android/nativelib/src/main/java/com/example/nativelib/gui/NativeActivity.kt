package com.example.nativelib.gui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.nativelib.NativeLib
import com.example.nativelib.R
import com.example.nativelib.models.api.streaming.DataStreams
import com.example.nativelib.models.api.streaming.StreamsResponse
import com.google.android.material.snackbar.Snackbar

class NativeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var instant: NativeActivity? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configure()
        instant = this
    }

    override fun onClick(v: View?) {
        val bundle = intent.getBundleExtra("Bundle")
        val streamsResponse = bundle?.getParcelable<StreamsResponse>("streamsResponse")

        when {
            v!!.id == R.id.bBack -> {
                closeActivity()
            }
            v.id == R.id.bAvailable -> {
                if (streamsResponse?.body?.availableStreams?.isNotEmpty() == true) {
                    setListAdapter(streamsResponse.body.availableStreams!!)
                } else {
                    "'available streams' not found".showSnackBar()
                }
            }
            v.id == R.id.bPlanned -> {
                if (streamsResponse?.body?.plannedStreams?.isNotEmpty() == true) {
                    setListAdapter(streamsResponse.body.plannedStreams!!)
                } else {
                    "'available streams' not found".showSnackBar()
                }
            }
            else -> {
                println("missing")
            }
        }
    }

    private fun setListAdapter(list: ArrayList<DataStreams>) {
        val adapter = StreamAdapter(
            this,
            list
        )

        val listView: ListView = findViewById(R.id.listview_1)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->

            when (val element = adapter.getItem(position)) {
                is DataStreams -> {
                    println(element.name)
                    val intent = Intent(this, WebViewActivity::class.java)
                    intent.putExtra("streamId", element.id)
                    intent.putExtra("appId", element.application)

                    val domain = NativeLib.getConfig()?.domain
                    intent.putExtra("domain", domain)
                    startActivity(intent)
                }
                else -> {
                    println("Type not found")
                }
            }
        }
    }

    private fun closeActivity() {
        finish()
    }

    private fun configure() {
        setContentView(R.layout.activity_native)
        buttonsConfigure()
    }

    private fun String.showSnackBar() {
        val parentLayout = findViewById<View>(android.R.id.content)
        val snack = Snackbar.make(parentLayout, this, Snackbar.LENGTH_LONG)
        snack.show()
    }

    private fun buttonsConfigure() {
        val bAvailable: Button? = findViewById(R.id.bAvailable)
        bAvailable?.setOnClickListener(this)

        val bPlanned: Button? = findViewById(R.id.bPlanned)
        bPlanned?.setOnClickListener(this)

        val bBack: Button? = findViewById(R.id.bBack)
        bBack?.setOnClickListener(this)
    }
}
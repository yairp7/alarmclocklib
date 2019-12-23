package com.pech.libs.alarmclocklib

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class AlarmActivity : AppCompatActivity() {
    var txtView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        txtView = findViewById(R.id.txt)

        load()
    }

    fun load() {
        txtView!!.isVisible = true
    }
}

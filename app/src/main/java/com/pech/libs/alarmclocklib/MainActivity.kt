package com.pech.libs.alarmclocklib

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.util.*

class MainActivity : AppCompatActivity() {
    var dateBtn: Button? = null
    var timeBtn: Button? = null
    var alarmBtn: Button? = null

    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var hour: Int = 0
    var minute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dateBtn = findViewById(R.id.dateBtn)
        timeBtn = findViewById(R.id.timeBtn)
        alarmBtn = findViewById(R.id.alarmBtn)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        dateBtn!!.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener {
                    _, y, m, d ->
                        dateBtn!!.text = "$d/${m + 1}/$y"
                        this.year = y
                        this.month = m + 1
                        this.day = d
                }, year, month, day)
            datePickerDialog.show()
        }

        timeBtn!!.setOnClickListener {
            val timePickerDialog = TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener {
                        _, h, m ->
                    timeBtn!!.text = "$h:$m"
                    this.hour = h
                    this.minute = m
                }, hour, minute, true)
            timePickerDialog.show()
        }

        alarmBtn!!.setOnClickListener {

        }
    }
}

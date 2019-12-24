package com.pech.libs.alarmclocklib

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.pech.libs.alarmclock.AlarmLib
import com.pech.libs.alarmclock.alarms.BaseAlarm
import com.pech.libs.alarmclock.database.AlarmsDatabase
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

    private var workerThread: HandlerThread? = null
    private var workerHandler: Handler? = null

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
                        this.month = m
                        this.day = d
                }, year, month, day)
            datePickerDialog.show()
        }

        timeBtn!!.setOnClickListener {
            val timePickerDialog = TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener {
                        _, h, m ->
                    var mStr: String = m.toString()
                    if(m < 10) mStr = "0$mStr"
                    timeBtn!!.text = "$h:$mStr"
                    this.hour = h
                    this.minute = m
                }, hour, minute, true)
            timePickerDialog.show()
        }

        workerThread = HandlerThread("AlarmsWorkerThread")
        workerThread!!.start()
        workerHandler = Handler(workerThread!!.looper)

        workerHandler!!.post {
            AlarmLib.init(AlarmsDatabase.buildDatabase(applicationContext))
        }

        alarmBtn!!.setOnClickListener {
            workerHandler!!.post {
                addAlarm()
            }
        }
    }

    private fun addAlarm() {
        val c = Calendar.getInstance()
        val listAlarms: List<BaseAlarm>? = AlarmLib.getAlarms()
        val alarmId: Int = listAlarms!!.size + 1
        c.set(this.year, this.month, this.day, this.hour, this.minute)
        val alarmTimeInMillis = c.timeInMillis
        val alarm: TestAlarm = TestAlarm(alarmId, alarmTimeInMillis, "{}")
        AlarmLib.addAlarm(applicationContext, alarm)
    }
}

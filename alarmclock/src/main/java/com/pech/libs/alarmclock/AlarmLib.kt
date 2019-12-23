package com.pech.libs.alarmclock

import android.content.Context
import com.pech.libs.alarmclock.alarms.BaseAlarm
import com.pech.libs.alarmclock.database.Alarm
import com.pech.libs.alarmclock.database.AlarmsDatabase
import java.lang.Exception

object AlarmLib {
    var initiated: Boolean = false
    private var alarmsDatabase: AlarmsDatabase? = null

    fun init(alarmsDatabase: AlarmsDatabase) {
        this.alarmsDatabase = alarmsDatabase
        initiated = true
    }

    fun destroy() {
        this.alarmsDatabase = null
        initiated = false
    }

    fun getAlarms(): List<BaseAlarm>? {
        if(!initiated) throw Exception("Must call init first!")
        if(alarmsDatabase == null) throw NullPointerException()

        val list: List<Alarm>? = alarmsDatabase!!.alarmDao().getAll()
        if(list != null) {
            return list.map { BaseAlarm.toAlarm(it) }
        }

        return null
    }

    fun addAlarm(alarm: BaseAlarm?) {
        synchronized(this) {
            if(!initiated) throw Exception("Must call init first!")
            if (alarmsDatabase == null) throw NullPointerException()
            if (alarm == null) throw NullPointerException()
            alarmsDatabase!!.alarmDao().insert(alarm.toEntity())
        }
    }

    fun deleteAlarm(alarmId: Int) {
        synchronized(this) {
            if(!initiated) throw Exception("Must call init first!")
            if (alarmsDatabase == null) throw NullPointerException()
            alarmsDatabase!!.alarmDao().delete(alarmId)
        }
    }
}
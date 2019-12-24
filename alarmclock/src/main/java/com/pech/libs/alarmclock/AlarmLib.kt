package com.pech.libs.alarmclock

import android.content.Context
import android.os.Looper
import androidx.annotation.WorkerThread
import com.pech.libs.alarmclock.alarms.BaseAlarm
import com.pech.libs.alarmclock.database.Alarm
import com.pech.libs.alarmclock.database.AlarmsDatabase
import com.pech.libs.alarmclock.utils.AlarmUtils

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

    @WorkerThread
    fun getAlarms(): List<BaseAlarm>? {
        AlarmUtils.isWorkerThread()
        if(!initiated) throw Exception("Must call init first!")
        if(alarmsDatabase == null) throw NullPointerException()

        val list: List<Alarm>? = alarmsDatabase!!.alarmDao().getAll()
        if (list != null) {
            return list.map { BaseAlarm.toAlarm(it) }
        }

        return null
    }

    @WorkerThread
    fun addAlarm(context: Context, alarm: BaseAlarm?) {
        AlarmUtils.isWorkerThread()
        synchronized(this) {
            if(!initiated) throw Exception("Must call init first!")
            if (alarmsDatabase == null) throw NullPointerException()
            if (alarm == null) throw NullPointerException()
            alarmsDatabase!!.alarmDao().insert(alarm.toEntity())
            AlarmUtils.setAlarm(context, alarm.getTimeInMillis(), alarm.getId())
        }
    }

    @WorkerThread
    fun deleteAlarm(context: Context, alarmId: Int) {
        AlarmUtils.isWorkerThread()
        synchronized(this) {
            if(!initiated) throw Exception("Must call init first!")
            if (alarmsDatabase == null) throw NullPointerException()
            alarmsDatabase!!.alarmDao().delete(alarmId)
            AlarmUtils.disableAlarm(context, alarmId)
        }
    }
}
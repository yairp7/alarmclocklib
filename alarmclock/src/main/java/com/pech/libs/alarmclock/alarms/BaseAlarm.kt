package com.pech.libs.alarmclock.alarms

import android.content.Context
import com.pech.libs.alarmclock.database.Alarm

abstract class BaseAlarm(private var alarmId: Int, private var timeInMillis: Long, private var extra: String) {
    companion object {
        const val ALARM_ID = "alarm_id"

        fun toAlarm(alarm: Alarm): BaseAlarm  {
            val alarmType = Class.forName(alarm.alarmType).kotlin
            val alarmInstance: BaseAlarm = alarmType.java.constructors[0].newInstance(alarm.alarmId, alarm.timeInMillis, alarm.extra) as BaseAlarm
            alarmInstance.setRepeated(alarm.isRepeated)
            return alarmInstance
        }
    }

    var isRepeated: Boolean = false
        private set

    fun toEntity(): Alarm {
        return Alarm(alarmId, javaClass.name, timeInMillis, isRepeated, extra)
    }

    fun setRepeated(isRepeated: Boolean) {
        this.isRepeated = isRepeated
    }

    fun updateTimeInMillis(timeInMillis: Long) {
        this.timeInMillis = timeInMillis
    }

    fun getId() = alarmId
    fun getTimeInMillis() = timeInMillis

    abstract fun load(context: Context?)
    abstract fun play(context: Context?)
}
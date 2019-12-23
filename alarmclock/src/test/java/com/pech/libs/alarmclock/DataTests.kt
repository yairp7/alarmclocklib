package com.pech.libs.alarmclock

import com.pech.libs.alarmclock.alarms.BaseAlarm
import com.pech.libs.alarmclock.database.Alarm
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DataTests {
    @Test
    fun alarmEntityToAlarmDataObject_test() {
        val alarmEntity: Alarm = Alarm(1,
            "com.pech.libs.alarmclock.alarms.SimpleAlarm",
            141242,
            false, "")

        val alarmDataObj = BaseAlarm.toAlarm(alarmEntity)

        val classType = Class.forName(alarmEntity.alarmType)

        assertTrue(alarmDataObj != null)
        assertTrue(alarmDataObj.javaClass.isAssignableFrom(classType))
    }
}

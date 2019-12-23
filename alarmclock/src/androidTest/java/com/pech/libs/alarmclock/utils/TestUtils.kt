package com.pech.libs.alarmclock.utils

import com.pech.libs.alarmclock.alarms.BaseAlarm
import com.pech.libs.alarmclock.alarms.SimpleAlarm
import com.pech.libs.alarmclock.database.Alarm
import kotlin.random.Random

class TestUtils {
    companion object {
        fun randAlarmEntity(): Alarm {
            val alarmId: Int =
                randInt()
            val timeInMillis: Long =
                randLong()
            return Alarm(alarmId, SimpleAlarm::class.java.name, timeInMillis, randBoolean(), "")
        }

        fun randAlarmEntityWithSpecificTimeAndRepeated(timeInMillis: Long, isRepeated: Boolean): Alarm {
            val alarmId: Int =
                randInt()
            return Alarm(alarmId, SimpleAlarm::class.java.name, timeInMillis, isRepeated, "")
        }

        fun randRepeatedAlarmEntity(): Alarm {
            val alarmId: Int =
                randInt()
            val timeInMillis: Long =
                randLong()
            return Alarm(alarmId, SimpleAlarm::class.java.name, timeInMillis, true, "")
        }

        fun randAlarmDataObj(): BaseAlarm {
            val alarmEntity: Alarm = randAlarmEntity()
            return BaseAlarm.toAlarm(alarmEntity)
        }

        fun randInt(): Int {
            return Random(System.currentTimeMillis()).nextInt(0, 1000)
        }

        private fun randLong(): Long {
            return Random(System.currentTimeMillis()).nextLong(0, 1000)
        }

        private fun randBoolean(): Boolean {
            return Random(System.currentTimeMillis()).nextBoolean()
        }
    }
}
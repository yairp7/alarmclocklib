package com.pech.libs.alarmclock.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.pech.libs.alarmclock.AlarmReceiver
import com.pech.libs.alarmclock.alarms.BaseAlarm
import com.pech.libs.alarmclock.database.Alarm
import com.pech.libs.alarmclock.database.AlarmDao
import com.pech.libs.alarmclock.database.AlarmsDatabase
import java.lang.Exception

class AlarmUtils {
    companion object {
        /**
         * Setup an alarm with an alarmId to be used later to retrieve
         * the specific alarm's data, and alarmCode to be used to disable the alarm.
         */
        fun setAlarm(context: Context, timeInMillis: Long, alarmId: String, alarmCode: Int) {
            val alarmManager: AlarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(BaseAlarm.ALARM_ID, alarmId)
            val pendingIntent = PendingIntent.getBroadcast(context, alarmCode, intent, 0)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
            }
        }

        /**
         * Disable an alarm using the alarmCode used in the setAlarm method
         */
        fun disableAlarm(context: Context, alarmCode: Int) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, alarmCode, intent, 0)
            alarmManager.cancel(pendingIntent)
        }

        private fun getNextAlarmTimeInMillis(alarmTimeInMillis: Long): Long {
            val currentTimeInMillis: Long = System.currentTimeMillis()
            if(alarmTimeInMillis < currentTimeInMillis) throw Exception("alarmTimeInMillis must be bigger than current time!");

            val timeToClosestAlarmInMillis: Long = alarmTimeInMillis - currentTimeInMillis
            val dayInMillis: Long = 1000 * 60 * 60 * 24
            return timeToClosestAlarmInMillis + dayInMillis
        }

        fun rescheduleAlarmIfNeeded(alarmsDatabase: AlarmsDatabase?, alarmObj: BaseAlarm) {
            if(alarmObj.isRepeated) {
                val nextAlarmTimeInMillis: Long = alarmObj.getTimeInMillis() + getNextAlarmTimeInMillis(alarmObj.getTimeInMillis())
                if (alarmsDatabase != null) {
                    val alarmDao: AlarmDao = alarmsDatabase.alarmDao()
                    alarmDao.delete(alarmObj.getId())
                    alarmObj.updateTimeInMillis(nextAlarmTimeInMillis)
                    alarmDao.insert(alarmObj.toEntity())
                }
            }
        }

        fun loadAlarm(alarmsDatabase: AlarmsDatabase?, alarmId: Int): BaseAlarm? {
            if(alarmId == -1 || alarmsDatabase == null) {
                return null
            }

            var alarmObj: BaseAlarm? = null

            val alarmDao: AlarmDao = alarmsDatabase.alarmDao()
            val alarmEntity: Alarm? = alarmDao.getById(alarmId)
            if(alarmEntity != null) {
                alarmObj = BaseAlarm.toAlarm(alarmEntity)
            }

            return alarmObj
        }
    }
}
package com.pech.libs.alarmclock

import android.app.IntentService
import android.content.Intent
import com.pech.libs.alarmclock.alarms.BaseAlarm
import com.pech.libs.alarmclock.database.AlarmsDatabase
import com.pech.libs.alarmclock.utils.AlarmUtils

class AlarmService: IntentService("AlarmService") {
    var alarmsDatabase: AlarmsDatabase? = null

    override fun onHandleIntent(intent: Intent?) {
        val alarmId: Int = intent!!.getIntExtra(BaseAlarm.ALARM_ID, -1)
        if(alarmId > -1) {
            alarmsDatabase = AlarmsDatabase.buildDatabase(applicationContext)
            val alarmObj: BaseAlarm? = AlarmUtils.loadAlarm(alarmsDatabase, alarmId)
            if(alarmObj != null) {
                AlarmUtils.rescheduleAlarmIfNeeded(alarmsDatabase, alarmObj) // Setup the same alarm for the next day
                alarmObj.load(applicationContext)
                alarmObj.play(applicationContext)
            }
        }
    }
}
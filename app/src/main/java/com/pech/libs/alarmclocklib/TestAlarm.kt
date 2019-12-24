package com.pech.libs.alarmclocklib

import android.content.Context
import android.content.Intent
import com.pech.libs.alarmclock.alarms.BaseAlarm

class TestAlarm(alarmId: Int, timeInMillis: Long, extra: String):
    BaseAlarm(alarmId, timeInMillis, extra) {
    override fun load(context: Context?) {

    }

    override fun play(context: Context?) {
        val intent = Intent(context, AlarmActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context!!.startActivity(intent)
    }
}
package com.pech.libs.alarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent: Intent = Intent(context, AlarmService::class.java).apply {
            this.putExtras(intent!!.extras!!)
        }
        context!!.startService(serviceIntent)
    }
}
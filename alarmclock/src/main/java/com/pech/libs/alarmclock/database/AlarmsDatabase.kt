package com.pech.libs.alarmclock.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1)
abstract class AlarmsDatabase: RoomDatabase() {
    abstract fun alarmDao() : AlarmDao

    companion object {
        const val DATABASE_NAME = "Alarms"

        fun buildDatabase(context: Context): AlarmsDatabase {
            return Room.databaseBuilder(context,
                AlarmsDatabase::class.java,
                DATABASE_NAME)
                .build()
        }
    }
}
package com.pech.libs.alarmclock.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): List<Alarm>?

    @Query("SELECT * FROM alarm WHERE alarmId = :alarmId")
    fun getById(alarmId: Int): Alarm?

    @Insert
    fun insert(vararg alarms: Alarm)

    @Query("DELETE FROM alarm WHERE alarmId = :alarmId")
    fun delete(alarmId: Int)
}
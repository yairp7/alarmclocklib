package com.pech.libs.alarmclock.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm(@PrimaryKey val alarmId: Int,
                 @ColumnInfo(name = "alarmType") val alarmType: String,
                 @ColumnInfo(name = "timeInMillis") val timeInMillis: Long,
                 @ColumnInfo(name = "isRepeated") val isRepeated: Boolean,
                 @ColumnInfo(name = "extra") val extra: String?)
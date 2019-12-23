package com.pech.libs.alarmclock

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pech.libs.alarmclock.alarms.BaseAlarm
import com.pech.libs.alarmclock.database.Alarm
import com.pech.libs.alarmclock.database.AlarmDao
import com.pech.libs.alarmclock.database.AlarmsDatabase
import com.pech.libs.alarmclock.utils.TestUtils
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SingletonInstrumentedTests {
    private lateinit var context: Context
    private lateinit var alarmDao: AlarmDao
    private lateinit var db: AlarmsDatabase

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AlarmsDatabase::class.java).build()
        alarmDao = db.alarmDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test(expected = Exception::class)
    fun notWorkingWithoutInit() {
        AlarmLib.destroy()

        val alarmDataObj: BaseAlarm = TestUtils.randAlarmDataObj()
        AlarmLib.addAlarm(context, alarmDataObj)
    }

    @Test
    @Throws(Exception::class)
    fun addAlarm() {
        AlarmLib.init(db)

        val alarmDataObj: BaseAlarm = TestUtils.randAlarmDataObj()
        AlarmLib.addAlarm(context, alarmDataObj)

        val alarmEntity: Alarm? = alarmDao.getById(alarmDataObj.getId())
        Assert.assertNotNull(alarmEntity)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAlarm() {
        AlarmLib.init(db)

        val alarmDataObj: BaseAlarm = TestUtils.randAlarmDataObj()
        AlarmLib.addAlarm(context, alarmDataObj)

        var alarmEntity: Alarm? = alarmDao.getById(alarmDataObj.getId())
        Assert.assertNotNull(alarmEntity)

        AlarmLib.deleteAlarm(context, alarmDataObj.getId())

        alarmEntity = alarmDao.getById(alarmDataObj.getId())
        Assert.assertNull(alarmEntity)
    }
}
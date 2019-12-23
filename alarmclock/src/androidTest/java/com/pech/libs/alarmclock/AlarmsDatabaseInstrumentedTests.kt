package com.pech.libs.alarmclock

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pech.libs.alarmclock.alarms.BaseAlarm
import com.pech.libs.alarmclock.database.Alarm
import com.pech.libs.alarmclock.database.AlarmDao
import com.pech.libs.alarmclock.database.AlarmsDatabase
import com.pech.libs.alarmclock.utils.AlarmUtils
import com.pech.libs.alarmclock.utils.TestUtils
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AlarmsDatabaseInstrumentedTests {
    private lateinit var alarmDao: AlarmDao
    private lateinit var db: AlarmsDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AlarmsDatabase::class.java).build()
        alarmDao = db.alarmDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAlarm() {
        val alarm: Alarm = TestUtils.randAlarmEntity()
        alarmDao.insert(alarm)

        val byId = alarmDao.getById(alarm.alarmId)
        Assert.assertEquals(byId, alarm)

        val alarms: List<Alarm>? = alarmDao.getAll()
        Assert.assertNotNull(alarms)
        Assert.assertTrue(alarms!!.size == 1)
        Assert.assertEquals(alarms[0], alarm)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAlarm() {
        val alarm: Alarm = TestUtils.randAlarmEntity()
        alarmDao.insert(alarm)

        var byId = alarmDao.getById(alarm.alarmId)
        Assert.assertEquals(byId, alarm)

        var alarms: List<Alarm>? = alarmDao.getAll()
        Assert.assertNotNull(alarms)
        Assert.assertTrue(alarms!!.size == 1)
        Assert.assertEquals(alarms[0], alarm)

        alarmDao.delete(alarm.alarmId)
        byId = alarmDao.getById(alarm.alarmId)
        Assert.assertTrue(byId == null)

        alarms = alarmDao.getAll()
        Assert.assertNotNull(alarms)
        Assert.assertTrue(alarms!!.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun rescheduleAlarm() {
        val alarm: Alarm = TestUtils.randAlarmEntityWithSpecificTimeAndRepeated(System.currentTimeMillis() + 10000, true)
        alarmDao.insert(alarm)

        var alarmObj = BaseAlarm.toAlarm(alarm)
        val timeInMillis = alarmObj.getTimeInMillis()

        AlarmUtils.rescheduleAlarmIfNeeded(db, alarmObj)

        var byId = alarmDao.getById(alarm.alarmId)
        Assert.assertTrue(byId!!.timeInMillis - timeInMillis > (24 * 60 * 60 * 1000))
    }

    @Test
    @Throws(Exception::class)
    fun loadAlarm() {
        val alarm: Alarm = TestUtils.randAlarmEntityWithSpecificTimeAndRepeated(System.currentTimeMillis() + 10000, true)

        val alarmObj: BaseAlarm? = AlarmUtils.loadAlarm(db, alarm.alarmId)
        Assert.assertNotNull(alarmObj)
    }
}

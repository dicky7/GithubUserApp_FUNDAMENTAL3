package com.example.favoriteconsumer.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.favoriteconsumer.MainActivity
import com.example.favoriteconsumer.R
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    companion object{
        const val EXTRA_MESAGE = "mesage"
        const val EXTRA_TYPE = "type"

        private const val TIME_NOTIFICATION = "09:00"
        private const val ID_REPEATING = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val message = intent.getStringExtra(EXTRA_MESAGE)
        showAlarmNotification(context,message)
    }

    private fun showAlarmNotification(context: Context,  message: String?){
        val channelId = "Dicky_1"
        val channelName = "Alarm Manager Daily"

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingInt = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_ONE_SHOT)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                .setContentTitle("Github Daily Reminder")
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(alarmSound)
                .setContentIntent(pendingInt)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(100,notification)
    }

    fun setRepeatingAlarm(context: Context, type: String, message: String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESAGE, message)
        intent.putExtra(EXTRA_TYPE,type)

        val timeArr = TIME_NOTIFICATION.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArr[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArr[1]))
        calendar.set(Calendar.SECOND,0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING,intent,0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
        Toast.makeText(context,"Repeating Alarm Set Up",Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show()
    }
}
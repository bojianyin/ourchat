package com.app.ourchat.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.app.ourchat.R
import com.app.ourchat.ui.activity.MainActivity
import kotlin.random.Random

class NotificationUtil {
    companion object{

        private val notificationIdList = arrayListOf<Int>()

        val CHANNEL_ID = "baby_message_2"

        //通知ID
        private const val NOTIFICATION_ID = 10000001
        //唯一的通知通道的ID
        private const val foregroundNotificationChannelId = "foreground_service_id_01"


        fun pushNotification(context: Context,title:String,content:String):Int{
            val nid = Random.nextInt(1000)
            val createNotification = createNotification(context, title, content)
            notificationIdList.add(nid)
            with(NotificationManagerCompat.from(context)) {
                notify(nid, createNotification.build())
            }

            return nid
        }

        fun cancelAll(context:Context){
            notificationIdList.clear()
            NotificationManagerCompat.from(context).cancelAll()
        }

        fun createNotification(context: Context,title:String,content:String): NotificationCompat.Builder {
            createNotificationChannel(context)
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            var notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ourphoto)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            return notification
        }

        fun createNotificationChannel(context: Context,channelId:String = CHANNEL_ID){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.channel_name)
                val descriptionText = context.getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(channelId, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }


        /**
         * 开启前台服务并发送通知
         */
        fun startForegroundWithNotification(service:Service){
            //8.0及以上注册通知渠道
            createNotificationChannel(service,foregroundNotificationChannelId)
            val notification: Notification = createNotification(service,"service running","").apply {
                setOngoing(true)
                setWhen(System.currentTimeMillis())
            }.build()
            //将服务置于启动状态 ,NOTIFICATION_ID指的是创建的通知的ID
            service.startForeground(NOTIFICATION_ID, notification)
            //发送通知到状态栏
            val notificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notification)
        }

    }
}
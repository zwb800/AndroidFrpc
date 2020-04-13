package com.example.frp

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class FrpcService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            when(intent!!.getIntExtra("command",-1)){
                COMMAND_STARTSERVICE->{
                    FrpTask.execute(intent.getStringExtra("ini"))
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        val notificationChannel = NotificationChannel("default","channel_name",
                            NotificationManager.IMPORTANCE_DEFAULT)
                        notificationManager.createNotificationChannel(notificationChannel)
                    }

                    val intentMain = Intent(this,MainActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(this,0,intent,0)
                    val notification = NotificationCompat.Builder(this,"default")
                    .setContentTitle("Frpc")
                    .setContentText("运行中")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)//一定要设置图标 不然就显示系统默认的通知
                    .build()
                    startForeground(1,notification)

                }

                COMMAND_STOPSERVICE->{
                    frpc.Frpc.stop()
                }
            }


        return super.onStartCommand(intent, flags, startId)
    }

    companion object{
        const val COMMAND_STARTSERVICE = 0
        const val COMMAND_STOPSERVICE = 1
        fun startService(context: Context,ini:String){
            val intent = Intent(context,FrpcService::class.java)
            intent.putExtra("command",COMMAND_STARTSERVICE)
            intent.putExtra("ini",ini)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
    1
        }

        fun stopService(context: Context){
            val intent = Intent(context,FrpcService::class.java)
            intent.putExtra("command",COMMAND_STOPSERVICE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }
    }

    object FrpTask : AsyncTask<String, Void, Int>(){
        override fun doInBackground(vararg params: String): Int {
            frpc.Frpc.start(params.get(0))
            return 0
        }
    }


}

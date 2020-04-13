package com.example.frp

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ini = getPreferences(Context.MODE_PRIVATE).getString("frpc_ini","[common]\n" +
                "server_addr = 43.249.193.233\n" +
                "server_port = 7000\n" +
                "tcp_mux = true\n" +
                "pool_count = 1\n" +
                "protocol = kcp\n" +
                "user = user\n" +
                "token = SakuraFrpClientToken\n" +
                "dns_server = 114.114.114.114\n" +
                " \n" +
                "[ssh]\n" +
                "privilege_mode = true\n" +
                "type = tcp\n" +
                "local_ip = 127.0.0.1\n" +
                "local_port = 22\n" +
                "remote_port = 60793\n" +
                "use_encryption = false\n" +
                "use_compression = true\n" +
                " \n" +
                "[FPV]\n" +
                "privilege_mode = true\n" +
                "type = udp\n" +
                "local_ip = 192.168.43.1\n" +
                "local_port = 5600\n" +
                "remote_port = 56208\n" +
                "use_encryption = false\n" +
                "use_compression = false\n" +
                " \n" +
                "[mavlink]\n" +
                "privilege_mode = true\n" +
                "type = udp\n" +
                "local_ip = 127.0.0.1\n" +
                "local_port = 14550\n" +
                "remote_port = 56209\n" +
                "use_encryption = false\n" +
                "use_compression = false\n" +
                " ")
        frpc_ini.setText(ini)

        btn_start.setOnClickListener {
            getPreferences(Context.MODE_PRIVATE).edit()
                .putString("frpc_ini",frpc_ini.text.toString())
                .apply()
            btn_start.visibility = View.GONE
            btn_stop.visibility = View.VISIBLE


            FrpcService.startService(this,frpc_ini.text.toString())
        }

        btn_stop.setOnClickListener {
            btn_start.visibility = View.VISIBLE
            btn_stop.visibility = View.GONE
            FrpcService.stopService(this)
        }
    }




    override fun onDestroy() {
        super.onDestroy()
    }
}

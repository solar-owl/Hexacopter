package com.example.hexacopterapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.NetworkOnMainThreadException
import android.os.StrictMode
import android.os.SystemClock
import android.text.format.DateFormat
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hexacopterapp.view.HexaSurfaceView
import com.example.hexacopterapp.view.HexaView
import com.google.gson.Gson
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import java.io.ByteArrayOutputStream
import java.lang.Math.toDegrees
import java.util.*
import kotlin.concurrent.fixedRateTimer
import androidx.gridlayout.widget.GridLayout



class MainActivity : AppCompatActivity() {


    private var session: Session ? = null
    private val channel: ChannelExec ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val dateFormat = DateFormat.getDateFormat(
            applicationContext
        )
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        labelUpdate(0.0, 0.0, 0.0)
        //setContentView(HexaSurfaceView(this))
        val HView = findViewById<HexaSurfaceView>(R.id.hexaView)
        HView.setWillNotDraw(false)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val jsch = JSch()


        try {
            //sshConnection.connect("172.20.10.3")
//          //sshConnection.authPassword("kopter", "kopter")
            session = jsch.getSession("tim", "192.168.0.105", 22)
            session?.setPassword("02181820")

            // Avoid asking for key confirmation
            val prop = Properties()
            prop.put("StrictHostKeyChecking", "no")
            session?.setConfig(prop)
            session?.connect()

//            // SSH Channel
//            val channel = session?.openChannel("exec") as ChannelExec
//            val stream = ByteArrayOutputStream()
//            channel.outputStream = stream

            //channel.setCommand("ls -la");
            //channel.connect(1000);
            java.lang.Thread.sleep(500);   // this kludge seemed to be required.
            //channel.disconnect();

            //val result = stream.toString();
        } catch (e: NetworkOnMainThreadException) {
            val text = "Нет соединения с гексакоптером!"
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(applicationContext, text, duration)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
            val intent = Intent(this@MainActivity, Connection::class.java)
            startActivity(intent)
        }

    }



    override fun onSaveInstanceState(savedInstanceState:Bundle) {
        super.onSaveInstanceState(savedInstanceState)
    }


    fun onClickTakeOff(view : View) {
        if (session != null){
            // SSH Channel
            val channel = session?.openChannel("exec") as ChannelExec
            val stream = ByteArrayOutputStream()
            channel.outputStream = stream
            //val HView = findViewById<HexaSurfaceView>(R.id.hexaView)
            //channel.setCommand("python3 TakeOff.py --connect /dev/serial0")
            //channel.connect(1000)
            //java.lang.Thread.sleep(500)   // this kludge seemed to be required.
            //smoothAnimation(-55f, 15f, 30f)
            fixedRateTimer("timer", false, 0, 50){
                //this@MainActivity.GetNewData{
                    if (session != null){
                        //channel.setCommand("python3 arm_test.py --connect /dev/serial0")
                        //channel.connect(10)
                        //java.lang.Thread.sleep(5)   // this kludge seemed to be required.
                        channel.setCommand("cat docs/flight.json")
                        channel.connect(1000)
                        java.lang.Thread.sleep(500)   // this kludge seemed to be required.
                        val result = stream.toString()
                        var gson = Gson()
                        var data = gson?.fromJson(result, Metrics.Data::class.java)
                        smoothAnimation(toDegrees(data.roll), toDegrees(data.pitch),toDegrees(data.yaw))
                        labelUpdate(toDegrees(data.roll), toDegrees(data.pitch),toDegrees(data.yaw))
                    }
                //}
            }
        }
        else{
            val text = "Потеряно соединение!"
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(applicationContext, text, duration)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()

            val intent = Intent(this@MainActivity, Connection::class.java)
            startActivity(intent)
        }
    }

    fun onClickLanding(view : View) {
        if (session != null) {
            //val HView = findViewById<HexaSurfaceView>(R.id.hexaView)
            // SSH Channel
            val channel = session?.openChannel("exec") as ChannelExec
            val stream = ByteArrayOutputStream()
            channel.outputStream = stream
            channel.setCommand("python3 Landing.py --connect /dev/serial0");
            channel.connect(10)
            java.lang.Thread.sleep(5);   // this kludge seemed to be required.
            //smoothAnimation(-5f, 10f, 70f)
        }
        else{
            val text = "Потеряно соединение!"
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(applicationContext, text, duration)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()

            val intent = Intent(this@MainActivity, Connection::class.java)
            startActivity(intent)
        }
    }

    fun smoothAnimation(new_cren: Double, new_tang: Double, new_risk: Double) {
        val HView = findViewById<HexaSurfaceView>(R.id.hexaView)
        HView.angle_cren_old = HView.angle_cren
        HView.angle_tang_old = HView.angle_tang
        HView.angle_risk_old = HView.angle_risk
        var cren_k : Double = (new_cren - HView.angle_cren_old) / 10f
        var tang_k : Double = (new_tang - HView.angle_tang_old) / 10f
        var risk_k : Double = (new_risk - HView.angle_risk_old) / 10f
        Log.d("MyLog", HView.angle_cren.toString())
        Log.d("MyLog", HView.angle_cren_old.toString())
        Log.d("MyLog", cren_k.toString())
        for (i in 1..10) {
            Log.d("MyLog", HView.angle_cren.toString())
            HView.angle_cren = HView.angle_cren + cren_k.toFloat()
            HView.angle_tang = HView.angle_tang + tang_k.toFloat()
            HView.angle_risk = HView.angle_risk + risk_k.toFloat()
            labelUpdate(HView.angle_cren.toDouble(), HView.angle_tang.toDouble(), HView.angle_risk.toDouble())
            SystemClock.sleep(25)
        }
    }

    fun labelUpdate(new_cren: Double, new_tang: Double, new_risk: Double) {
        val cren = findViewById<TextView>(R.id.cren)
        val tang = findViewById<TextView>(R.id.tang)
        val risk = findViewById<TextView>(R.id.risk)
        cren.text = "Крен: " + new_cren.toString()
        tang.text = "Тангаж: " + new_tang.toString()
        risk.text = "Угол рысканья: " + new_risk.toString()
    }

    override fun onDestroy() {
        channel?.disconnect()
        super.onDestroy()

    }


}

class Metrics {

    data class Data(
        val pitch: Double,
        val yaw:  Double,
        val roll: Double,
    )
}
package com.example.hexacopterapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.NetworkOnMainThreadException
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
import com.google.gson.Gson
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.IOUtils
import net.schmizz.sshj.connection.channel.direct.Session
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.Error
import java.net.Socket
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {


    private var session: Session ? = null
    private val sshConnection : Session ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val dateFormat = DateFormat.getDateFormat(
            applicationContext
        )
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        labelUpdate(0f, 0f, 0f)
        setContentView(HexaSurfaceView(this))
        val HView = findViewById<HexaSurfaceView>(R.id.hexaView)
        HView.setWillNotDraw(false)

        try{
            val sshConnection = SSHClient()
//            sshConnection.connect("172.20.10.3:22")
//            sshConnection.authPassword("kopter", "kopter")
            sshConnection.connect("192.168.0.105")
            sshConnection.authPassword("tim", "02181820")
            session = sshConnection.startSession()
            Thread.sleep(1000)
            fixedRateTimer("timer", false, 0, 50){
                this@MainActivity.GetNewData{
                    if (session != null){
                        var sess = session
                        val cmd = sess?.exec("spc flight.json")
                        val tempNum = IOUtils.readFully(cmd?.getInputStream()).toString()
                        var gson = Gson()
                        var data = gson?.fromJson("flight.json", Metrics.Data::class.java)
                        smoothAnimation(data.roll, data.pitch,data.yaw)
                        labelUpdate(data.roll, data.pitch,data.yaw)
                    }
                }
            }
        }
        catch ( e: NetworkOnMainThreadException){
            val text = "Пора покормить кота!"
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(applicationContext, text, duration)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
            val intent = Intent(this@MainActivity, Connection::class.java)
            startActivity(intent)
        }
    }

    fun GetNewData(function: () -> Unit) {}

    fun onClickTakeOff(view : View) {
        if (session != null){
        //val HView = findViewById<HexaSurfaceView>(R.id.hexaView)
        val cmd = session?.exec("python TakeOff.py")
        val tempNum = IOUtils.readFully(cmd?.getInputStream()).toString()
        //smoothAnimation(-55f, 15f, 30f)
        }
        else{
            val text = "Пора покормить кота!"
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
            val cmd = session?.exec("python Landing.py")
            val tempNum = IOUtils.readFully(cmd?.getInputStream()).toString()
            //smoothAnimation(-5f, 10f, 70f)
        }
        else{
            val text = "Пора покормить кота!"
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(applicationContext, text, duration)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()

            val intent = Intent(this@MainActivity, Connection::class.java)
            startActivity(intent)
        }
    }

    fun smoothAnimation(new_cren : Float, new_tang : Float, new_risk : Float) {
        val HView = findViewById<HexaSurfaceView>(R.id.hexaView)
        HView.angle_cren_old = HView.angle_cren
        HView.angle_tang_old = HView.angle_tang
        HView.angle_risk_old = HView.angle_risk
        var cren_k : Float = (new_cren - HView.angle_cren_old) / 10f
        var tang_k : Float = (new_tang - HView.angle_tang_old) / 10f
        var risk_k : Float = (new_risk - HView.angle_risk_old) / 10f
        Log.d("MyLog", HView.angle_cren.toString())
        Log.d("MyLog", HView.angle_cren_old.toString())
        Log.d("MyLog", cren_k.toString())
        for (i in 1..10) {
            Log.d("MyLog", HView.angle_cren.toString())
            HView.angle_cren = HView.angle_cren + cren_k
            HView.angle_tang = HView.angle_tang + tang_k
            HView.angle_risk = HView.angle_risk + risk_k
            labelUpdate(HView.angle_cren, HView.angle_tang, HView.angle_risk)
            SystemClock.sleep(25)
        }
    }

    fun labelUpdate(new_cren : Float, new_tang : Float, new_risk : Float) {
        val cren = findViewById<TextView>(R.id.cren)
        val tang = findViewById<TextView>(R.id.tang)
        val risk = findViewById<TextView>(R.id.risk)
        cren.text = "Крен: " + new_cren.toString()
        tang.text = "Тангаж: " + new_tang.toString()
        risk.text = "Угол рысканья: " + new_risk.toString()
    }

    override fun onDestroy() {
        sshConnection?.close()
        super.onDestroy()

    }


}

class Metrics {

    data class Data(
        val roll: Float,
        val pitch: Float,
        val yaw:  Float
    )
}
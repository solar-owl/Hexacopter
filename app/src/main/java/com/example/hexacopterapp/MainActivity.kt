package com.example.hexacopterapp

import android.app.Activity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.hexacopterapp.view.HexaSurfaceView
import com.example.hexacopterapp.view.HexaView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        labelUpdate(0f, 0f, 0f)
        //setContentView(HexaSurfaceView(this))
        //val HView = findViewById<HexaSurfaceView>(R.id.hexaView)
        //HView.setWillNotDraw(false)
    }

    fun onClickTakeOff(view : View) {
        val HView = findViewById<HexaSurfaceView>(R.id.hexaView)
        smoothAnimation(-55f, 15f, 30f)
    }

    fun onClickLanding(view : View) {
        val HView = findViewById<HexaSurfaceView>(R.id.hexaView)
        smoothAnimation(-5f, 10f, 70f)
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

    override fun onResume() {
        super.onResume()
    }


}
package com.example.hexacopterapp

import android.app.Activity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.hexacopterapp.view.HexaView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val HView = findViewById<HexaView>(R.id.hexaView)
        HView.setWillNotDraw(false)
    }

    fun onClickTakeOff(view : View) {
        val txt = findViewById<TextView>(R.id.textView23)
        txt.text = "sss"
        val HView = findViewById<HexaView>(R.id.hexaView)
        HView.angle_cren = -55f
        HView.angle_tang = 15f
        HView.angle_risk = 30f
        HView.invalidate()
        Log.d("MyLog", txt.text.toString())
        Log.d("MyLog", HView.angle_cren.toString())
    }

    fun onClickLanding(view : View) {
        val txt = findViewById<TextView>(R.id.textView23)
        txt.text = "aaa"
        val HView = findViewById<HexaView>(R.id.hexaView)
        HView.angle_cren = -25f
        HView.angle_tang = -15f
        HView.angle_risk = -125f
        HView.invalidate()
        Log.d("MyLog", HView.angle_cren.toString())
    }

    override fun onResume() {
        val i = 1
        val HView = findViewById<HexaView>(R.id.hexaView)
        super.onResume()
        //while (true) {
            //for (i in 0..30) {
                //SystemClock.sleep(1000)
                //HView.angle_cren = i.toFloat()
            //}
        //}
    }


}
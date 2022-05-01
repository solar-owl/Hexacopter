package com.example.hexacopterapp


//import net.schmizz.sshj.connection.channel.direct.Session
import android.content.Intent
import android.os.Bundle
import android.os.NetworkOnMainThreadException
import android.os.StrictMode
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session
import java.io.ByteArrayOutputStream
import java.lang.reflect.InvocationTargetException
import java.net.ConnectException
import java.util.Properties


private var session: Session? = null

class Connection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)
    }

    fun onClick(view : View) {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val jsch = JSch()

        session = jsch.getSession("kopter", "192.168.0.104", 22)
        session?.setPassword("kopter")

            // Avoid asking for key confirmation
        val prop = Properties()
        prop.put("StrictHostKeyChecking", "no")

        session?.setConfig(prop)
        try {
            session?.connect()

            // SSH Channel
            val channel = session?.openChannel("exec") as ChannelExec
            val stream = ByteArrayOutputStream()
            channel.outputStream = stream

            //channel.setCommand("ls -la");
            channel.connect(350);
            java.lang.Thread.sleep(50)   // this kludge seemed to be required.
            channel.disconnect()

            val result = stream.toString();
            val intent = Intent(this@Connection, MainActivity::class.java)
            startActivity(intent)
        }
        catch ( e: Exception){

            val text = "Нет соединения с гексакоптером!"
            val duration = Toast.LENGTH_LONG

            val toast = Toast.makeText(applicationContext, text, duration)
            toast.setGravity(Gravity.CENTER, 0, 0)

//            val toastContainer = toast.view as LinearLayout
//            val catImage = ImageView(this)
//            catImage.setImageResource(R.drawable.angrycat)
//            toastContainer.addView(catImage, 0)

            toast.show()
//            val intent = Intent(this@Connection, MainActivity::class.java)
//            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(savedInstanceState:Bundle) {
        super.onSaveInstanceState(savedInstanceState)
    }

}




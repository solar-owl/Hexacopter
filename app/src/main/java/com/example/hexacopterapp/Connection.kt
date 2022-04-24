package com.example.hexacopterapp


import android.content.Intent
import android.os.Bundle
import android.os.NetworkOnMainThreadException
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.common.IOUtils
import net.schmizz.sshj.connection.channel.direct.Session


private var session: Session? = null
private val sshConnection : Session? = null

class Connection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)
    }

    fun onClick(view : View) {

//        val client = Socket("127.0.0.1", 14550)
//        val output = PrintWriter(client.getOutputStream(), true)
//        val input = BufferedReader(InputStreamReader(client.inputStream))
//
//        println("Client sending [Hello]")
//        output.println("Hello")
//        println("Client receiving [${input.readLine()}]")
//        client.close()
//    }

        try{
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val sshConnection = SSHClient()
            sshConnection.addHostKeyVerifier(NullHostKeyVerifier())
            //sshConnection.connect("172.20.10.3")
            //sshConnection.authPassword("kopter", "kopter")
            //sshConnection.connect(InetAddress.getByName("192.168.0.105"))
            //sshConnection.loadKnownHosts()
            //sshConnection.connect("localhost")
            sshConnection.connect("192.168.0.105")
            sshConnection.authPassword("tim", "02181820")
            session = sshConnection.startSession()
            val cmd = session?.exec("ls -l")
            val tempNum = IOUtils.readFully(cmd?.getInputStream()).toString()
            //Thread.sleep(1000)
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(applicationContext, tempNum, duration)
            toast.setGravity(Gravity.CENTER, 0, 0)

            toast.show()
            val intent = Intent(this@Connection, MainActivity::class.java)
            startActivity(intent)
        }
        catch ( e: NetworkOnMainThreadException){
            val text = "Нет соединения с гексакоптером!"
            val duration = Toast.LENGTH_LONG

            val toast = Toast.makeText(applicationContext, text, duration)
            toast.setGravity(Gravity.CENTER, 0, 0)

            val toastContainer = toast.view as LinearLayout
            val catImage = ImageView(this)
            catImage.setImageResource(R.drawable.angrycat)
            toastContainer.addView(catImage, 0)

            toast.show()

//            val intent = Intent(this@Connection, MainActivity::class.java)
//            startActivity(intent)
        }
    }
}




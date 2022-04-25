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


        session = jsch.getSession("tim", "192.168.0.105", 22)
        session?.setPassword("02181820")
//        session = jsch.getSession("kopter", "172.20.10.3", 22)
//        session?.setPassword("kopter")

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

            channel.setCommand("ls -la");
            channel.connect(1000);
            java.lang.Thread.sleep(500)   // this kludge seemed to be required.
            channel.disconnect()

            val result = stream.toString();
//        try{
//            val policy = ThreadPolicy.Builder().permitAll().build()
//            StrictMode.setThreadPolicy(policy)
//            val sshConnection = SSHClient()
//            sshConnection.addHostKeyVerifier(PromiscuousVerifier())
//            //sshConnection.connect("172.20.10.3")
//            //sshConnection.authPassword("kopter", "kopter")
//            //sshConnection.connect(InetAddress.getByName("192.168.0.105"))
//            //sshConnection.loadKnownHosts()
//            //sshConnection.connect("localhost")
//            //sshConnection.connect("192.168.0.105")
//            //sshConnection.authPassword("tim", "02181820")
//            val keyProvider: KeyProvider = sshConnection.loadKeys(PRIVATE_KEY.toString(), null, null)
//            sshConnection.authPublickey("tim", keyProvider)
//            session = sshConnection.startSession()
//            val cmd = session?.exec("ls -l")
//            val tempNum = IOUtils.readFully(cmd?.getInputStream()).toString()
//            //Thread.sleep(1000)
            //val duration = Toast.LENGTH_LONG
            //val toast = Toast.makeText(applicationContext, tempNum, duration)
            //toast.setGravity(Gravity.CENTER, 0, 0)

            //toast.show()
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




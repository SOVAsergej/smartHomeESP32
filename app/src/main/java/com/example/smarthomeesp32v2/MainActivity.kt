package com.example.SmartHomeESP32v2

import com.example.SmartHomeESP32v2.R
import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.net.UnknownHostException

class MainActivity : Activity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val light1State = false
		val light2State = false
		val rebootButton = findViewById<Button>(R.id.rebootButton)
		val pali1Button = findViewById<Button>(R.id.pali1)
		val gasi1Button = findViewById<Button>(R.id.gasi1)
		val pali2Button = findViewById<Button>(R.id.pali2)
		val gasi2Button = findViewById<Button>(R.id.gasi2)
		val paliAButton = findViewById<Button>(R.id.paliA)
		val gasiAButton = findViewById<Button>(R.id.gasiA)
		val status = findViewById<Button>(R.id.status)
		val responseText = findViewById<TextView>(R.id.responseText)

		fun doSmth(command: String) {
			CoroutineScope(Dispatchers.Main).launch {
				responseText.text = "Connecting..."
				try {
					val response = withContext(Dispatchers.IO) {
						Socket("192.168.1.36", 80).use { socket ->
							val output = PrintWriter(socket.getOutputStream(), true)
							val input = BufferedReader(InputStreamReader(socket.getInputStream()))
							output.println("$command")
							input.readLine() ?: "No response"
						}
					}
					responseText.text = response
				} catch (e: UnknownHostException) {
					responseText.text = "Error: Cannot connect to server"
				} catch (e: Exception) {
					responseText.text = "Error: ${e.message}"
				}
			}
		}

		rebootButton.setOnClickListener {
			doSmth("REBOOT")
		}
		status.setOnClickListener {
			doSmth("STATUS")
		}
		pali1Button.setOnClickListener {
			doSmth("PALI_1")
		}
		gasi1Button.setOnClickListener {
			doSmth("GASI_1")
		}
		pali2Button.setOnClickListener {
			doSmth("PALI_2")
		}
		gasi2Button.setOnClickListener {
			doSmth("GASI_2")
		}
		paliAButton.setOnClickListener {
			doSmth("PALI_A")
		}
		gasiAButton.setOnClickListener {
			doSmth("GASI_A")
		}
	}
}
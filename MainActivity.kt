package com.example.smsgatewaystarter

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var phoneInput: EditText
    private lateinit var messageInput: EditText
    private lateinit var statusText: TextView
    private val smsPermissionRequest = 4001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 36, 28, 28)
            setBackgroundColor(0xFFF4F7FB.toInt())
        }
        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 0, 0, 24)
        }
        content.addView(TextView(this).apply {
            text = "SMS Gateway"
            textSize = 28f
            setTextColor(0xFF123B70.toInt())
        })
        content.addView(TextView(this).apply {
            text = "SIM SMS test • Manual sending only"
            textSize = 14f
            setTextColor(0xFF536579.toInt())
            setPadding(0, 6, 0, 24)
        })
        content.addView(label("Phone number"))
        phoneInput = EditText(this).apply {
            hint = "Recipient number (e.g. 01XXXXXXXXX)"
            inputType = android.text.InputType.TYPE_CLASS_PHONE
            singleLine = true
        }
        content.addView(phoneInput, matchWrap())
        content.addView(label("SMS message"))
        messageInput = EditText(this).apply {
            hint = "Message to send"
            minLines = 3
            gravity = Gravity.TOP
        }
        content.addView(messageInput, matchWrap())
        content.addView(Button(this).apply {
            text = "Send test SMS"
            setOnClickListener { requestPermissionOrSend() }
        }, matchWrap())
        statusText = TextView(this).apply {
            text = "Status: Ready. Test only with a number you own or have permission to message."
            textSize = 14f
            setTextColor(0xFF26384A.toInt())
            setPadding(0, 18, 0, 8)
        }
        content.addView(statusText)
        content.addView(TextView(this).apply {
            text = "Starter prototype only: no Firebase/API connection yet. SMS charges may apply. Do not use for unsolicited or bulk messages."
            textSize = 13f
            setTextColor(0xFF7A4B00.toInt())
            setPadding(0, 8, 0, 0)
        })
        scroll.addView(content)
        root.addView(scroll)
        setContentView(root)
    }

    private fun label(value: String) = TextView(this).apply {
        text = value
        textSize = 15f
        setTextColor(0xFF23384D.toInt())
        setPadding(0, 12, 0, 4)
    }

    private fun matchWrap() =
        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    private fun requestPermissionOrSend() {
        val number = phoneInput.text.toString().trim()
        val message = messageInput.text.toString().trim()
        if (number.isBlank() || message.isBlank()) {
            Toast.makeText(this, "Enter a phone number and message", Toast.LENGTH_SHORT).show()
            return
        }
        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS), smsPermissionRequest)
            return
        }
        sendSms(number, message)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == smsPermissionRequest && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendSms(phoneInput.text.toString().trim(), messageInput.text.toString().trim())
        } else if (requestCode == smsPermissionRequest) {
            statusText.text = "Status: SMS permission denied."
        }
    }

    @Suppress("DEPRECATION")
    private fun sendSms(number: String, message: String) {
        try {
            val smsManager = if (android.os.Build.VERSION.SDK_INT >= 31) getSystemService(SmsManager::class.java)
                else SmsManager.getDefault()
            smsManager.sendMultipartTextMessage(number, null, smsManager.divideMessage(message), null, null)
            statusText.text = "Status: SMS send request submitted to Android. Delivery is not guaranteed."
            Toast.makeText(this, "SMS send request submitted", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            statusText.text = "Status: Failed — ${e.message ?: "unknown error"}"
            Toast.makeText(this, "SMS failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

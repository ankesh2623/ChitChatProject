package com.example.chitchat
/*
once the login signup is done we should move to the main activity
val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
 */
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signuptxt =findViewById<TextView>(R.id.SignUpText)
        signuptxt.setOnClickListener{
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
         val forgotpasswordtxt=findViewById<TextView>(R.id.ForgotPasswordText)
        forgotpasswordtxt.setOnClickListener {
            val intent=Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }
}

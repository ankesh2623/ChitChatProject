package com.example.chitchat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chitchat.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding:ActivityForgotPasswordBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        binding= ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=Firebase.auth

        binding.SendInstructionButton.setOnClickListener{
            if(checkFiled()){
                val email=binding.emailET?.text.toString().trim()
                auth.sendPasswordResetEmail(email).addOnSuccessListener {
                    Toast.makeText(this,"Check mail",Toast.LENGTH_SHORT).show()
                    val gotogmail=findViewById<TextView>(R.id.goToGmailText)
                    gotogmail.text="Go to Gmail"
                    gotogmail.setOnClickListener {
                        val intent=Intent(Intent.ACTION_SENDTO)
                        intent.data= Uri.parse("mailto:")
                        startActivity(intent)
                    }
                }.addOnFailureListener{
                    Toast.makeText(this,"Invalid User",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    private fun checkFiled(): Boolean{
        val email=binding.emailET?.text.toString().trim()
        if(email==""){
            Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show()
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Enter proper email :(",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
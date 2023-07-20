package com.example.chitchat

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chitchat.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth=Firebase.auth
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SignUpText.setOnClickListener{
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.ForgotPasswordText.setOnClickListener {
            val intent=Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

//        val intent=Intent(this,MainActivity::class.java)
//        startActivity(intent)
//        the above code should be executed when there is successful login
        binding.LoginButton.setOnClickListener{
            if(checkAllFields()){
                val email=binding.emailEditText?.text.toString().trim()
                val password=binding.passwordEditText?.text.toString()
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if(it.isSuccessful){
                        if(auth.currentUser?.isEmailVerified==true){
                            val intent=Intent(this,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(this,"Signup/Email verification/Password failure",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun checkAllFields():Boolean{
        val email =binding.emailEditText?.text.toString().trim()
        if(email==""){
            Toast.makeText(this,"Email is a required field", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Check email format", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.passwordEditText?.text.toString()==""){
            Toast.makeText(this,"Password is a required filed", Toast.LENGTH_SHORT).show()
        }
        if(binding.passwordEditText?.text.toString().length<6){
            Toast.makeText(this,"Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}

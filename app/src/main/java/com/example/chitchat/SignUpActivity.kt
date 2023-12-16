package com.example.chitchat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chitchat.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var databaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth=Firebase.auth
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SignUpButton.setOnClickListener{
            if(checkAllFields()){
                val name = binding.nameEditText?.text.toString()
                val email=binding.emailEditText?.text.toString().trim()
                val password=binding.passwordEditText?.text.toString()
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                    if(it.isSuccessful){
                        auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                            Toast.makeText(this,"Check mail, an email is sent for verification",Toast.LENGTH_SHORT).show()
                            val gotogmail=findViewById<TextView>(R.id.goToGmailText)
                            gotogmail.text="Go to Gmail"
                            binding.goToGmailText?.setOnClickListener {
                                val intent=Intent(Intent.ACTION_SENDTO)
                                intent.data= Uri.parse("mailto:")
                                startActivity(intent)
                            }
                        }?.addOnFailureListener{
                            Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
                        }
                        addUserToDatabase(email,name, auth.currentUser?.uid!!)
                        auth.signOut()
                    }
                    else{
                        Log.e("error", it.exception.toString())
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String){
        databaseRef= FirebaseDatabase.getInstance().getReference()
        databaseRef.child("user").child(uid).setValue(User(email,name,uid))
    }

    private fun checkAllFields(): Boolean{
        val email =binding.emailEditText?.text.toString().trim()
        if(email==""){
            Toast.makeText(this,"Email is a required field",Toast.LENGTH_SHORT).show()
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Check email format",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.passwordEditText?.text.toString()==""){
            Toast.makeText(this,"Password is a required filed",Toast.LENGTH_SHORT).show()
        }
        if(binding.passwordEditText?.text.toString().length<6){
            Toast.makeText(this,"Password should be at least 6 characters",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.confirmPasswordEditText?.text.toString()==""){
            Toast.makeText(this,"Confirm Password is required filed",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.confirmPasswordEditText?.text.toString()!=binding.passwordEditText?.text.toString()){
            Toast.makeText(this,"Password did not match",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
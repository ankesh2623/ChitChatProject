package com.example.chitchat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbref=FirebaseDatabase.getInstance().getReference()

        auth = Firebase.auth

        userList = ArrayList()
        adapter=UserAdapter(this, userList)

        userRecyclerView=findViewById(R.id.userRecyclerView)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter= adapter

        dbref.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()  // whenever the data is changed we will have this loop run so we need to clear previous data otherwise the data will get repeated
                for(postSnapshot in snapshot.children){
                    val currentuser = postSnapshot.getValue(User:: class.java)

                    try {
                        // Your code that may cause an error or exception
                        if (auth.currentUser?.uid != currentuser?.uid) {
                                userList.add(currentuser!!)
                        }

                    } catch (e: Exception) {
                        // Log the error message and stack trace
                        Log.e("MainActivity", "An error occurred: ${e.message}", e)
                    }



                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}
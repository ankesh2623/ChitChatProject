package com.example.chitchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chitchat.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var dbref: DatabaseReference
    private lateinit var messageList: ArrayList<Message>

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        dbref = FirebaseDatabase.getInstance().getReference()
        binding=ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val name = intent.getStringExtra("name")
        val receiveruid = intent.getStringExtra("uid")
        val senderuid= FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = receiveruid + senderuid
        receiverRoom= senderuid+receiveruid

        supportActionBar?.title= name


        val chatRecyclerView= binding.chatrecyclerview
        val msgBox = binding.msgBox
        val sendBtn = binding.sendbutton

        messageList= ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        chatRecyclerView.layoutManager= LinearLayoutManager(this)
        chatRecyclerView.adapter=messageAdapter

        //adding data to recyclerview
        dbref.child("chats").child(senderRoom!!).child("messages").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for(postsnapshot in snapshot.children){
                    val message = postsnapshot.getValue(Message::class.java)
                    messageList.add(message!!)
                }
                messageAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        sendBtn.setOnClickListener{
            val msg = msgBox.text.toString()
            val messageObj = Message(msg, senderuid)

            dbref.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObj).addOnSuccessListener {
                    dbref.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObj)
                }
            msgBox.text.clear()
        }


    }

}
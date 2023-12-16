package com.example.chitchat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    val itemreceived = 1
    val itemsent=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType==1){
            // inflate received
            val view : View = LayoutInflater.from(context).inflate(R.layout.received, parent, false)
            return  receivedViewHolder(view)
        }
        else {
            val view : View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return sentViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentmsg= messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentmsg.senderId)){
            return itemsent
        }
        else return itemreceived
    }
    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentmsg = messageList[position]
        if(holder.javaClass==sentViewHolder::class.java){
            // for sent view holder
            val viewHolder = holder as sentViewHolder
            viewHolder.sentmsg.text= currentmsg.message

        }
        else{
            // for received view holder
             val viewHolder = holder as receivedViewHolder
            viewHolder.receivedmsg.text=currentmsg.message
        }

    }

    class sentViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val sentmsg= itemView.findViewById<TextView>(R.id.sentmsg)
    }
    class receivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val receivedmsg = itemView.findViewById<TextView>(R.id.receivedmsg)

    }

}
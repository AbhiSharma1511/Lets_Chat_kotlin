package com.kotlin.letschat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kotlin.letschat.Adapters.UserChatAdapter
import com.kotlin.letschat.Models.UserChat

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var imgSent: ImageView
    private lateinit var messageAdapter: UserChatAdapter
    private lateinit var messageList: ArrayList<UserChat>
    private lateinit var mDbRef: DatabaseReference

    var senderRoom: String? = null
    var receiverRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val intent = intent
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid+senderUid
        receiverRoom = senderUid+receiverUid

        supportActionBar?.title = name

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.edtMessage)
        imgSent = findViewById(R.id.imgSend)

        messageList = ArrayList()
        messageAdapter = UserChatAdapter(this,messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter  =messageAdapter

        imgSent.setOnClickListener{
            uploadChats(senderUid!!)
        }
        loadChats()
    }

    fun uploadChats(senderUid:String){
        val message = messageBox.text.toString()
        val messageObject = UserChat(message,senderUid)
        mDbRef.child("Chats").child(senderRoom!!).child("messages").push()
            .setValue(messageObject)
            .addOnSuccessListener {
                mDbRef.child("Chats").child(receiverRoom!!).child("messages").push()
                    .setValue(messageObject)
            }
        messageBox.setText("")
    }

    fun loadChats(){
        mDbRef.child("Chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(UserChat::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }
}
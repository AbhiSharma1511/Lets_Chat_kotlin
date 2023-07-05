package com.kotlin.letschat

import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kotlin.letschat.Adapters.UserAdapter
import com.kotlin.letschat.MenuActivity.ProfileActivity
import com.kotlin.letschat.MenuActivity.SettingActivity
import com.kotlin.letschat.Models.User

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var button: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var dataRef: DatabaseReference
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)

//        setSupportActionBar(toolbar)

//        supportActionBar?.title = baseContext

        auth = FirebaseAuth.getInstance()
        dataRef = FirebaseDatabase.getInstance().getReference()

        recyclerView = findViewById(R.id.rv)
        button = findViewById(R.id.button)

        userList = arrayListOf<User>()
//        adapter = UserAdapter(this@MainActivity,userList)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = adapter

        button.setOnClickListener {
            auth.signOut()
            val intent = Intent(this,LoginLayout::class.java)
            startActivity(intent)
            finish()
        }
//        showUserList()
        dataRef.child("users").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children){
                    val user = postSnapshot.getValue(User::class.java)
                    if(user?.uid != auth.currentUser?.uid ){
                        userList.add(user!!)
                    }
                }
                val adapter = UserAdapter(this@MainActivity,userList)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu1, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profileMenu -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.settingMenu -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.logoutMenu -> {
                auth.signOut()
                true
            }
            else -> true
        }
    }
}

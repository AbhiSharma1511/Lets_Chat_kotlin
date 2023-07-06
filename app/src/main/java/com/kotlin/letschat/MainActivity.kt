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

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var button: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var dataRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Let's Chat"

        auth = FirebaseAuth.getInstance()
        dataRef = FirebaseDatabase.getInstance().getReference()

        recyclerView = findViewById(R.id.rv)
        userList = arrayListOf<User>()
//        adapter = UserAdapter(this@MainActivity,userList)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = adapter


        /********************** showUserList() ***********************/

        dataRef.child("users").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
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

    /****************** Adding the custom menu in the main activity *******************/

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
                val intent = Intent(this,LoginLayout::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }
}

package com.kotlin.letschat

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kotlin.letschat.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    lateinit var auth: FirebaseAuth

    private lateinit var hashMap: HashMap<String, String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnSignup.setOnClickListener {
            val email = binding.edtEmailSignup.text.toString()
            val password = binding.edtPasswordSignup.text.toString()
            val username = binding.edtUsernameSignup.text.toString()

            if(email=="" && password==""){
                Toast.makeText(this,"Enter email/password", Toast.LENGTH_SHORT).show()
            }else{
                if(password.length<6){
                    Toast.makeText(this,"Password length must be > 6", Toast.LENGTH_SHORT).show()
                }else{
                    signUp(email, password,username)
                }
            }
        }



    }

    fun updateDetails(username: String, email: String) {

        val uid = auth.currentUser?.uid!!
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val dataReference: DatabaseReference = database.getReference("users")
        hashMap = HashMap()

        hashMap["email"] = auth.currentUser?.email!!
        hashMap["username"] = username
        hashMap["uid"] = uid
        hashMap["quote"] = "Hey, Let's chat..."

        dataReference.child(uid).setValue(hashMap)
        Toast.makeText(this,"User Added to database successfully.",Toast.LENGTH_SHORT).show()
    }

    private fun signUp(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(this,"Account created successfully.",Toast.LENGTH_SHORT).show()
                    updateDetails(username, email)
//                    val intent = Intent(this,MainActivity::class.java)
//                    startActivity(intent)
                    finish()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Use Different Id: "+task.exception, Toast.LENGTH_SHORT,).show()
//                    updateUI(null)
                }
            }
    }
}
package com.kotlin.letschat

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kotlin.letschat.databinding.ActivityLoginLayoutBinding

class LoginLayout : AppCompatActivity() {

    private lateinit var binding: ActivityLoginLayoutBinding
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()

        if(auth.currentUser!=null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString()
            val password = binding.edtPasswordLogin.text.toString()

            if(email=="" && password==""){
                Toast.makeText(this,"Enter email/password",Toast.LENGTH_SHORT).show()
            }else{
                signIn(email, password)
            }
        }

        binding.txtNewUser.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Toast.makeText(this,"Login successfully.",Toast.LENGTH_SHORT).show()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
//                Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT,).show()
            }
    }
}
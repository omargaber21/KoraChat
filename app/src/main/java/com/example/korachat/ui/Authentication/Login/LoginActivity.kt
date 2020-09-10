package com.example.korachat.ui.Authentication.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.AlteredCharSequence.make
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.korachat.R
import com.example.korachat.ui.Authentication.Register.RegisterActivity
import com.example.korachat.ui.HomeActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val loginActivityVM=ViewModelProvider(this).get(LoginActivityVM::class.java)
        btnLogin.setOnClickListener {
            progress_bar.visibility= View.VISIBLE
            btnLogin.isEnabled=false
            btnRegister.isEnabled=false
            loginActivityVM.Login(emailTIET.text.toString(),passwordTIET.text.toString(),this)

        }
        loginActivityVM.successfullLogin.observe(this, Observer {
            progress_bar.visibility= View.GONE
            btnLogin.isEnabled=true
            btnRegister.isEnabled=true
            if(it){
                startActivity(Intent(this, HomeActivity::class.java))
                Log.w("myTag","Signin s")
                this.finishAffinity()
            }else
                Toast.makeText(this, "invalid data", Toast.LENGTH_LONG).show()

        })
        btnRegister.setOnClickListener {
          val  intent:Intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        var mAuth:FirebaseAuth=FirebaseAuth.getInstance()
        val currentUser:FirebaseUser?=mAuth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            this.finish()
            }

    }
}

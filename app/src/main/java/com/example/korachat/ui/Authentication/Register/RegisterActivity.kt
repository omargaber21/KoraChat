package com.example.korachat.ui.Authentication.Register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.korachat.R
import com.example.korachat.models.Users
import com.example.korachat.ui.HomeActivity
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_register.*
import java.io.File

class RegisterActivity : AppCompatActivity() {
    lateinit var registerViewModel:RegisterActivityVM
    var imageUri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerViewModel = ViewModelProvider(this).get(RegisterActivityVM::class.java)
        register_btnregister.setOnClickListener {
            progress_bar.visibility= View.VISIBLE
            register_btnregister.isEnabled=false
            Register()
        }
        register_image.setOnClickListener{
            val intent=Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type="image/*"
            startActivityForResult(intent,2)
        }
        registerViewModel.successfullRegister.observe(this, Observer {
            progress_bar.visibility= View.GONE
            register_btnregister.isEnabled=true
            if (it) {
                if(imageUri!=null){
                    registerViewModel.uploadImage(imageUri!!)

                }
                startActivity(Intent(this, HomeActivity::class.java))
                finishAffinity()
            }else{
                Toast.makeText(this, "invalid data", Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun Register() {
        val newUser =Users()
        newUser.username=register_nameTIET.text.toString()
        newUser.email = register_emailTIET.text.toString()
        newUser.phone = register_phoneTIET.text.toString()
        newUser.favourite_team = register_favourite_team_TIET.text.toString()
        registerViewModel.Register(
            register_emailTIET.text.toString(),
            register_passwordTIET.text.toString(),
            this,
            newUser
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==2&&resultCode== Activity.RESULT_OK){
            if(data!=null){
                imageUri=data.data as Uri
                val imageFile=File(imageUri!!.path)
                //val compressedImageFile = Compressor.compress(this,imageFile)
                register_image.setImageURI(imageUri)

            }
        }
    }
}

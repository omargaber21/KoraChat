package com.example.korachat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.korachat.R
import com.example.korachat.ui.Authentication.Login.LoginActivity
import com.example.korachat.ui.Home.ChatsFragment.ChatsFragment
import com.example.korachat.ui.Home.SearchFragment.SearchFragment
import com.example.korachat.ui.Home.ProfileFragment.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
val mAuth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        supportActionBar!!.title=""
        val viewPagerAdapter=ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(ChatsFragment(),"Chats")
        viewPagerAdapter.addFragment(SearchFragment(),"Search")
        viewPagerAdapter.addFragment(ProfileFragment(),"Profile")
        view_pager.adapter=viewPagerAdapter
        tab_layout.setupWithViewPager(view_pager)
        val homeActivityVM=ViewModelProvider(this).get(HomeActivityVM::class.java)
        homeActivityVM.getCurrentUserDetails(this,mAuth.currentUser!!.uid)
        homeActivityVM.currentUserMLD.observe(this, Observer {
            user_name.text=it.username
            val url=it.imageURL
            if(url!=""){
                Glide.with(applicationContext)
                    .load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile_circle_imageview)
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.sign_out -> {mAuth.signOut()
            this.startActivity(Intent(this,LoginActivity::class.java))
            this.finishAffinity()}
        }
        return super.onOptionsItemSelected(item)
    }
}

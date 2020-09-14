package com.example.korachat.ui.Home.ProfileFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.example.korachat.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    val mAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profileFragmentVM = ViewModelProvider(this).get(ProfileFragmentVM::class.java)
        profileFragmentVM.getCurrentUserDetails(this,mAuth.currentUser?.uid!!)
        profileFragmentVM.currentUserMLD.observe(this, Observer {
            currentuser_name.text = it.username
            currentuser_fav.text = it.favourite_team
            Glide.with(this)
                .load(it.imageURL)
                .placeholder(R.drawable.ic_person_grey_24dp)
                .into(personal_image)
        })
        base_name.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(
                context!!,
                R.style.Theme_MaterialComponents_Light_BottomSheetDialog
            )
            val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet, parent_view)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }
    }
}

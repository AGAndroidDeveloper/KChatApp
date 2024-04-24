package com.ankitgupta.kchatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ankitgupta.kchatapp.authentication.MainActivity
import com.ankitgupta.kchatapp.databinding.ActivityUserProfileBinding
import com.ankitgupta.kchatapp.sharedpref.ProfileDataManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private var userProfileData: ProfileData? = ProfileData()
    private lateinit var profileDataManager: ProfileDataManager
    private lateinit var auth: FirebaseAuth
    private var mAuthListener: AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile)
        profileDataManager = ProfileDataManager(this)
        auth = Firebase.auth

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        mAuthListener = AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                println("User logged in")
            } else {
                Toast.makeText(this, "user logout successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                println("User not logged in")
            }
        }
        getUserDataFromPref()
        binding.backbtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.logoutUser.setOnClickListener {
            auth.signOut()
        }

    }

    private fun getUserDataFromPref() {
        userProfileData = profileDataManager.getProfileData("PROFILE_DATA")
    }

    override fun onStart() {
        super.onStart()
        if (userProfileData != null) {
            Glide.with(this)
                .load(userProfileData!!.imageURL)
                .centerCrop()
                .placeholder(R.drawable.download)
                .error(R.drawable.download)
                .into(binding.profileImage)

            binding.userName.text = userProfileData?.userName
            binding.emailAddress.text = userProfileData?.email
        }
    }
}
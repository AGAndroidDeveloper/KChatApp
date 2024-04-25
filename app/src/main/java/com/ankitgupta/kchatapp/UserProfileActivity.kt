package com.ankitgupta.kchatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ankitgupta.kchatapp.authentication.MainActivity
import com.ankitgupta.kchatapp.databinding.ActivityUserProfileBinding
import com.ankitgupta.kchatapp.sharedpref.ProfileDataManager
import com.ankitgupta.kchatapp.viewmodel.UserProfileViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuth.getInstance
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var auth: FirebaseAuth
    private var mAuthListener: AuthStateListener? = null
    private val viewmodel: UserProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile)

        setView()



        lifecycleScope.launch {
            viewmodel.profileData.flowWithLifecycle(
                minActiveState = Lifecycle.State.STARTED,
                lifecycle = lifecycle
            ).collect {
                updateUserProfileData(it)
            }
        }

        mAuthListener = AuthStateListener { fbAuth ->
            val user = fbAuth.currentUser
            if (user != null) {
                println("User logged in")
            } else {
                Toast.makeText(this, "user logout successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                println("User not logged in")
            }
        }
        clickEvent()
    }

    private fun setView() {
        auth = getInstance()

        Glide.with(this)
            .load(R.drawable.icons8_logout)
            .centerCrop()
            .into(binding.logoutImage)
//        userProfileChangeRequest {
//
//        }
    }

    private fun clickEvent() {
        binding.apply {
            headerLayout.backbtn.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            logoutLayout.setOnClickListener {
                auth.signOut()
            }

        }
    }

    private fun updateUserProfileData(it: ProfileData) {
        Glide.with(this)
            .load(it.imageURL)
            .centerCrop()
            .placeholder(R.drawable.download)
            .error(R.drawable.download)
            .into(binding.headerLayout.profileImage)
        binding.headerLayout.userName.text = it.userName
        binding.headerLayout.emailAddress.text = it.email
    }

    override fun onStart() {
        super.onStart()
        mAuthListener?.let { auth.addAuthStateListener(it) }
    }
}
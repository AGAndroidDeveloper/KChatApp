package com.ankitgupta.kchatapp.screen.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ankitgupta.kchatapp.model.ProfileData
import com.ankitgupta.kchatapp.R
import com.ankitgupta.kchatapp.application.HiltApplication
import com.ankitgupta.kchatapp.screen.activity.authentication.MainActivity
import com.ankitgupta.kchatapp.databinding.ActivityUserProfileBinding
import com.ankitgupta.kchatapp.response.FirebaseResultState
import com.ankitgupta.kchatapp.utill.GlideLoadImage
import com.ankitgupta.kchatapp.viewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuth.getInstance
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var auth: FirebaseAuth
    private var mAuthListener: AuthStateListener? = null
    private val viewmodel: UserProfileViewModel by viewModels()
    private val myapplication: HiltApplication = HiltApplication.instance
    private lateinit var glideLoadImageObj: GlideLoadImage
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewmodel.uploadProfileInFirebaseStorage(uri)
//                binding.headerLayout.profileImage.setImageURI(uri)
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile)
        setView()
        updateUserProfileFromLocalDb()
        handleStateOfProfileUpload()
        authStateListener()
        clickEvent()
    }

    private fun authStateListener() {
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
    }

    private fun updateUserProfileFromLocalDb() {
        lifecycleScope.launch {
            viewmodel.profileData.flowWithLifecycle(
                minActiveState = Lifecycle.State.STARTED,
                lifecycle = lifecycle
            ).collect {
                updateUserProfileData(it)
            }
        }
    }

    private fun handleStateOfProfileUpload() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewmodel.uploadProfileState.flowWithLifecycle(
                lifecycle = lifecycle,
                minActiveState = Lifecycle.State.STARTED
            ).collect { state ->
                when (state) {
                    FirebaseResultState.Idle -> {}
                    FirebaseResultState.Loading -> {
                        myapplication.spinnerStart(this@UserProfileActivity)
                    }

                    is FirebaseResultState.Failure -> {
                        myapplication.spinnerStop()
                    }

                    is FirebaseResultState.Success -> {
                        myapplication.spinnerStop()
                        myapplication.showToast(
                            this@UserProfileActivity,
                            "profile photo updated"
                        )

                        viewmodel.updateUser(state.data as Uri, auth)
//                        viewmodel.getUpdatedUserFromLocalStorage()
                       // showImageInProFileImageView(state.data as Uri)
                    }
                }

            }
        }

    }

    private fun showImageInProFileImageView(uri: Uri) {
        // update new uri also in firebase user node

        uri.let { it1 ->
            glideLoadImageObj.loadImage(
                binding.headerLayout.profileImage,
                it1.toString()
            )
        }

//        Glide.with(this)
//            .load(uri)
//            .centerCrop()
//            .placeholder(R.drawable.pngtree_gray_avatar_placeholder_png_image_3416697)
//            .error(R.drawable.pngtree_gray_avatar_placeholder_png_image_3416697)
//            .into(binding.headerLayout.profileImage)
    }

    private fun setView() {
        auth = getInstance()
        glideLoadImageObj = GlideLoadImage(this)
        glideLoadImageObj.loadLogOutGif(binding.logoutImage)
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

            headerLayout.editProfilePic.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//                myapplication.showToast(this@UserProfileActivity, "upload new profile picture")
            }


        }
    }

    private fun updateUserProfileData(it: ProfileData) {
        it.imageURL?.let { it1 ->
            glideLoadImageObj.loadImage(
                binding.headerLayout.profileImage,
                it1
            )
        }
        binding.headerLayout.userName.text = it.userName
        binding.headerLayout.emailAddress.text = it.email
    }

    override fun onStart() {
        super.onStart()
        mAuthListener?.let { auth.addAuthStateListener(it) }
    }
}
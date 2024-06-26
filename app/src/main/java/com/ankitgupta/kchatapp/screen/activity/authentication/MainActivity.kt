package com.ankitgupta.kchatapp.screen.activity.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ankitgupta.kchatapp.screen.activity.HomeActivity
import com.ankitgupta.kchatapp.model.ProfileData
import com.ankitgupta.kchatapp.R
import com.ankitgupta.kchatapp.application.HiltApplication
import com.ankitgupta.kchatapp.databinding.ActivityMainBinding
import com.ankitgupta.kchatapp.response.FirebaseResultState
import com.ankitgupta.kchatapp.utill.Constant.SERVER_CLIENT_ID
import com.ankitgupta.kchatapp.utill.Constant.TAG
import com.ankitgupta.kchatapp.viewmodel.AuthenticationViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myApplication: HiltApplication
    private var launcher: ActivityResultLauncher<Intent>? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val viewmodel: AuthenticationViewModel by viewModels()
    private val googleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(SERVER_CLIENT_ID)
            .requestEmail()
            .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        myApplication = HiltApplication.instance
        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(this@MainActivity, googleSignInOptions)
        googleSignInLauncher()

//        lifecycleScope.launch {
//            viewmodel.userState.flowWithLifecycle(
//                lifecycle = lifecycle,
//                minActiveState = Lifecycle.State.STARTED
//            ).collect { islogin ->
//                if (islogin) {
//                    binding.authenticate.visibility = View.GONE
//                } else {
//                    binding.authenticate.visibility = View.VISIBLE
//                }
//            }
//        }


        lifecycleScope.launch {
            viewmodel.observeProfileResponseState.flowWithLifecycle(
                lifecycle = lifecycle,
                minActiveState = Lifecycle.State.STARTED
            ).collect { state ->
                when (state) {
                    is FirebaseResultState.Idle -> {}
                    is FirebaseResultState.Loading -> {
                        myApplication.spinnerStart(this@MainActivity)
                    }

                    is FirebaseResultState.Success -> {
                        myApplication.spinnerStop()
                        Log.e("successData", "${state.data}")
                        myApplication.showToast(this@MainActivity, state.data as String)
                    }

                    is FirebaseResultState.Failure -> {
                        myApplication.spinnerStop()
                        Log.e("successData", state.errorMessage)
                        myApplication.showToast(this@MainActivity, state.errorMessage)
                    }
                }
            }
        }
        binding.authenticate.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            launcher?.launch(intent)
            // startActivityForResult(intent, REQ_ONE_TAP)
            //   authenticateWithGoogle()
        }

        binding.signOut.setOnClickListener {
            signOutCurrentUser()
        }
    }

    private fun googleSignInLauncher() {
        launcher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    try {
                        myApplication.spinnerStart(this@MainActivity)
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)!!
                        Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        myApplication.spinnerStop()
                        // Google Sign In failed, update UI appropriately
                        Log.w(TAG, "Google sign in failed", e)
                    }
                }
            }
    }

    private fun signOutCurrentUser() {
        googleSignInClient.signOut()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "${task.result}", Toast.LENGTH_SHORT).show()
                    binding.authenticate.visibility = View.VISIBLE
                    myApplication.clearSharedPreferences()
                } else {
                    Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it}", Toast.LENGTH_SHORT).show()
            }
    }


//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == REQ_ONE_TAP) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)!!
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e)
//            }
//        }
//    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val userProfile = user?.let {
                        ProfileData(
                            email = it.email,
                            uid = it.uid,
                            imageURL = it.photoUrl.toString(),
                            userName = it.displayName,
                            phoneNumber = it.phoneNumber
                        )
                    }

                    Log.e("user", "$userProfile")
                    if (userProfile != null) {
                        viewmodel.saveUserProfileDataInLocalStorage(userProfile)
                    }

                    // save user in db
                    userProfile?.let { viewmodel.saveUserInFireBase(profileData = it) }
                    myApplication.spinnerStop()
                    Log.e("user", "$userProfile")
                    updateUI(user)
                } else {
                    myApplication.spinnerStop()
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

//    private fun saveUserInDB(userProfile: ProfileData?) {
//        val userMapData = userProfile?.profileDataToMap()
//        val reference = realDb.getReference("users")
//        if (userProfile != null) {
//            userProfile.uid?.let { it ->
//                reference.child(it).setValue(userMapData)
//                    .addOnSuccessListener {
//                        Log.e(TAG, "data added in db successfully")
//                    }
//                    .addOnFailureListener {
//                        Log.e(TAG, "exception :${it.message}")
//                    }
//            }
//        }
//    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, "log in successfully", Toast.LENGTH_SHORT).show()
          //  viewmodel.updateUserState(true)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
          //  viewmodel.updateUserState(false)
            Toast.makeText(this, "no current user found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        launcher?.unregister()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
}
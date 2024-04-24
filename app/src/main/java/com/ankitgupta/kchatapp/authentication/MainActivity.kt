package com.ankitgupta.kchatapp.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ankitgupta.kchatapp.HomeActivity
import com.ankitgupta.kchatapp.ProfileData
import com.ankitgupta.kchatapp.R
import com.ankitgupta.kchatapp.application.HiltApplication
import com.ankitgupta.kchatapp.databinding.ActivityMainBinding
import com.ankitgupta.kchatapp.sharedpref.ProfileDataManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myApplication: HiltApplication
    private lateinit var profileDataManager: ProfileDataManager
    private var launcher: ActivityResultLauncher<Intent>? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(SERVER_CLIENT_ID)
            .requestEmail()
            .build()

    companion object {
        // TODO SERVER_CLIENT_ID must be put here
        const val SERVER_CLIENT_ID =
            "1016905926973-oo7lpaatjeftdj1uu112j9lmdqbht630.apps.googleusercontent.com"
        const val TAG = "MY_ACTIVITY_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        myApplication = HiltApplication.instance
        auth = Firebase.auth
        profileDataManager = ProfileDataManager(this)
        googleSignInClient = GoogleSignIn.getClient(this@MainActivity, googleSignInOptions)

        launcher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
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
                        profileDataManager.saveProfileData(userProfile, "PROFILE_DATA")
                    }

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

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            binding.authenticate.visibility = View.GONE
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            //        Toast.makeText(this, user.displayName, Toast.LENGTH_SHORT).show()
        } else {
            binding.authenticate.visibility = View.VISIBLE
            Toast.makeText(this, "no current user found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
}
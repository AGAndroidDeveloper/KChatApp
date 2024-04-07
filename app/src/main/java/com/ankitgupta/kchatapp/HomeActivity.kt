package com.ankitgupta.kchatapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ankitgupta.kchatapp.application.HiltApplication
import com.ankitgupta.kchatapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var myApplication: HiltApplication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //       enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        myApplication = HiltApplication.instance
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        binding.menu.setOnClickListener {
            showPopUpMenu(it)
        }
    }

    private fun showPopUpMenu(view: View) {
        myApplication.showPopupMenu(this, view) {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
package com.ankitgupta.kchatapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import com.ankitgupta.kchatapp.application.HiltApplication
import com.ankitgupta.kchatapp.databinding.ActivityHomeBinding
import com.ankitgupta.kchatapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var myApplication: HiltApplication
    private val viewmodel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //       enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        myApplication = HiltApplication.instance

        binding.backNavigation.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.menu.setOnClickListener {
            showPopUpMenu(it)
        }
    }

    private fun showPopUpMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        // Inflating popup menu from popup_menu.xml file
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile_ -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        // Showing the popup menu
        popupMenu.show()

    }


}
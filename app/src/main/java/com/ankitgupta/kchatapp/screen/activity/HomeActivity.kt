package com.ankitgupta.kchatapp.screen.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ankitgupta.kchatapp.R
import com.ankitgupta.kchatapp.adapter.HomeAllFriendAdapter
import com.ankitgupta.kchatapp.application.HiltApplication
import com.ankitgupta.kchatapp.databinding.ActivityHomeBinding
import com.ankitgupta.kchatapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var myApplication: HiltApplication
    private val viewmodel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeAllFriendAdapter
    private lateinit var friendRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //       enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setView()
        clickEvent()
    }

    private fun setView() {
        myApplication = HiltApplication.instance
        friendRecyclerView = binding.allFriendRecyclerView
        adapter = HomeAllFriendAdapter(this@HomeActivity)
        friendRecyclerView.adapter = adapter
    }

    private fun clickEvent() {
        binding.apply {
            homeTopBar.apply {
                backNavigation.setOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }

                menu.setOnClickListener {
                    showPopUpMenu(it)
                }
            }
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
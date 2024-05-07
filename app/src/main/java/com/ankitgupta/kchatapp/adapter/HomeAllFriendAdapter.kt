package com.ankitgupta.kchatapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ankitgupta.kchatapp.databinding.HomeUserItemBinding
import com.ankitgupta.kchatapp.model.HomeFriendModel
import com.ankitgupta.kchatapp.utill.GlideLoadImage

class HomeAllFriendAdapter(private val context: Context) :
    RecyclerView.Adapter<HomeAllFriendAdapter.ViewHolder>() {
    private var allFriendList: ArrayList<HomeFriendModel> = arrayListOf()
    private val glideObj = GlideLoadImage(context)

    @SuppressLint("NotifyDataSetChanged")
    fun setFriendList(list: ArrayList<HomeFriendModel>) {
        allFriendList.clear()
        allFriendList.addAll(list)
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: HomeUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val profileImage: ImageView = binding.profileImage
        private val name: TextView = binding.friendName
        private val lastMessage: TextView = binding.lastMessage

        fun bind(friend: HomeFriendModel) {
            if (friend.uri?.isAbsolute == true) {
                glideObj.loadImage(profileImage, friend.uri.toString())
            }

            name.text = friend.name ?: "N/A"
            lastMessage.text = friend.lastMessage ?: "no last  message found"

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeUserItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (allFriendList.isEmpty()) {
            0
        } else
            return allFriendList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = allFriendList[position]
        holder.bind(friend)
    }
}
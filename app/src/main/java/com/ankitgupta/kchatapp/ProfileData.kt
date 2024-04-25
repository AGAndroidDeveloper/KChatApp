package com.ankitgupta.kchatapp

data class ProfileData(
    val email: String? = null,
    val uid: String? = null,
    val imageURL: String? = null,
    val userName: String? = null,
    val phoneNumber: String? = null
) {
    fun profileDataToMap(): HashMap<String, String?> {
        val map = HashMap<String, String?>()
        map["email"] = email
        map["uid"] = uid
        map["imageURL"] = imageURL
        map["userName"] = userName
        map["phoneNumber"] = phoneNumber
        return map
    }

}





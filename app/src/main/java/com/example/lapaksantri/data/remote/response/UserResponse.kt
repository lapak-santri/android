package com.example.lapaksantri.data.remote.response


import com.example.lapaksantri.domain.entities.User
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("address")
    val address: Any,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: Any,
    @SerializedName("name")
    val name: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: Any,
    @SerializedName("updatedAt")
    val updatedAt: String
) {
    fun toEntity(): User {
        return User(
            name,
            email,
            "",
        )
    }
}
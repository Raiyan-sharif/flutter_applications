package com.acibd.serviceautonomous.networking.all_data

data class UserInfo(
    val company_id: String,
    val created_at: Any,
    val email: String,
    val email_verified_at: Any,
    val id: Int,
    val is_active: String,
    val name: String,
    val role_id: String,
    val updated_at: Any,
    val user_territory: UserTerritory,
    val username: String
)
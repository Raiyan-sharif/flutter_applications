package com.work.onlineleave.data.login

data class LoginResponse(
    val authcode: String,
    val deptcode: String,
    val deptname: String,
    val desgname: String,
    val emailid: String,
    val empcode: String,
    val empname: String,
    val group_id: String,
    val jobtypecategory: String,
    val jobtypecategorydeatils: String,
    val joiningdate: String,
    val menus: List<Menu>,
    val message: String,
    val password: String,
    val postaccess: String,
    val poweruser: String,
    val success: Int,
    val worklocation: String,
    val yearserved: String
)
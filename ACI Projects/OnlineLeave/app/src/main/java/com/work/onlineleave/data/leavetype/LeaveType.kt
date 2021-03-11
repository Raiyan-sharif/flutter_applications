package com.work.onlineleave.data.leavetype

data class LeaveType(
    val `data`: List<Data>,
    val success: Boolean,
    val total: Int
)
package com.work.onlineleave.data.leave_status

data class LeaveStatus(
    val `data`: List<Data>,
    val success: Boolean,
    val total: Int
)
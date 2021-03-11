package com.work.onlineleave.data.new_leave

data class NewLeaveResponse(
    val message: Message,
    val success: Boolean,
    val total: Int
)
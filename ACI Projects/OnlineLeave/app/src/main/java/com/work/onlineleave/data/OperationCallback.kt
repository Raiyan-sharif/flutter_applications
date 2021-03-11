package com.work.onlineleave.data

interface OperationCallback<T> {
    fun onError(error: String?)
    fun onSuccess(data: T)
}
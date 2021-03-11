package com.work.onlineleave.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.work.onlineleave.data.OperationCallback
import com.work.onlineleave.data.login.LoginResponse

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError = MutableLiveData<String>()
    val onMessageError: LiveData<String> = _onMessageError


    fun postLogin(userName: String, password: String) {
        _isViewLoading.postValue(true)
        repository.postLogin(userName, password, object : OperationCallback<LoginResponse> {
            override fun onSuccess(data: LoginResponse) {
                _isViewLoading.postValue(false)
                _loginResponse.postValue(data)
            }

            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }
        })
    }

    fun isViewLoading(value: Boolean) {
        _isViewLoading.postValue(value)
    }

}
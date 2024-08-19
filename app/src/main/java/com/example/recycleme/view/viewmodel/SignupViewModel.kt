package com.example.storyapp.signup

import androidx.lifecycle.ViewModel
import com.example.recycleme.data.remote.user.UserRepository

class SignupViewModel(private val repository: UserRepository): ViewModel() {
    fun register(name: String, email: String, password: String) = repository.signup(name, email, password)
}
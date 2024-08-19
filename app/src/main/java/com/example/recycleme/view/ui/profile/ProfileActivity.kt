package com.example.recycleme.view.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recycleme.databinding.ActivityProfileBinding
import com.example.recycleme.view.ui.home.HomeActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setupViewModel()

//        profileViewModel.getSession().observe(this) { user ->
//            binding.username.text = user.name
//        }

        binding.logoutButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

//    private fun setupViewModel() {
//        val pref = UserPref.getInstance(applicationContext.dataStore)
//        val repository = UserRepository.getInstance(ApiConfig.getApiService(), pref)
//        profileViewModel = ViewModelProvider(this, ProfileViewModelFactory(repository))[ProfileViewModel::class.java]
//    }
}
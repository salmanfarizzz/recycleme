package com.example.recycleme.view.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleme.R
import com.example.recycleme.view.adapter.NewsAdapter
import com.example.recycleme.databinding.ActivityNewsBinding
import com.example.recycleme.view.ui.home.HomeFragment
import com.example.recycleme.view.ui.profile.ProfileActivity
import com.example.recycleme.view.viewmodel.NewsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        replaceFragment(HomeFragment())

        bottomNavigationView = findViewById(R.id.menuBar)
        bottomNavigationView.selectedItemId = R.id.news
        binding.menuBar.setOnItemSelectedListener {
            when(it.itemId) {
//                R.id.home -> replaceFragment(HomeFragment())
                R.id.news -> {
                    startActivity(Intent(this, NewsActivity::class.java))
                    true
                }
                R.id.profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else ->false
            }
            true
        }

        initViewModel()
        setupRecyclerView()
        viewModel.getNews()
        observeViewModel()
    }

    private fun initViewModel() {
        viewModel = NewsViewModel()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@NewsActivity)
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvNews.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.rvNews.visibility = View.VISIBLE
            }
        }

        viewModel.news.observe(this) { news ->
            newsAdapter.submitList(news)
        }
    }

    fun openNewsUrl(view: View) {
        val url = view.getTag(R.id.tvLink) as? String
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
//    fun replaceFragment(fragment: Fragment) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.frame_layout, fragment)
//        fragmentTransaction.commit()
//    }
}
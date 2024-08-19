package com.example.recycleme.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recycleme.data.remote.response.NewsItem
import com.example.recycleme.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel {
    private val _news = MutableLiveData<List<NewsItem>>()
    val news: LiveData<List<NewsItem>> = _news

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getNews()
    }

    fun getNews() {
        _isLoading.value = true
        val client = ApiConfig
            .getApiService()
            .getTopHeadlines()
        client.enqueue(object : Callback<List<NewsItem>> {
            override fun onResponse(call: Call<List<NewsItem>>, response: Response<List<NewsItem>>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _news.value = responseBody
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<List<NewsItem>>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}

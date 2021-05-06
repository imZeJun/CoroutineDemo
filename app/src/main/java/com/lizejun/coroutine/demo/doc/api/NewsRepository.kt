package com.lizejun.coroutine.demo.doc.api

import com.lizejun.coroutine.demo.doc.converter.BaseConverterFactory
import com.lizejun.coroutine.demo.doc.data.BaseResult
import com.lizejun.coroutine.demo.doc.data.NewsItem
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NewsRepository private constructor() {

    private val client by lazy {
        OkHttpClient()
    }

    private val newsApi by lazy {
        Retrofit.Builder()
            .addConverterFactory(create())
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://gank.io")
            .client(client)
            .build().create(NewsApi::class.java)
    }

    suspend fun requestNews(): BaseResult<List<NewsItem>> {
        return newsApi.getNews()
    }

    companion object {
        val instance = Holder.holder
    }

    object Holder {
        val holder = NewsRepository()
    }

}
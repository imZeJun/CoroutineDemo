package com.lizejun.coroutine.demo.doc.api

import com.lizejun.coroutine.demo.doc.data.BaseResult
import com.lizejun.coroutine.demo.doc.data.NewsItem
import retrofit2.http.GET

interface NewsApi {
    @GET("api/v2/data/category/GanHuo/type/Android/page/1/count/10")
    suspend fun getNews(): BaseResult<List<NewsItem>>
}
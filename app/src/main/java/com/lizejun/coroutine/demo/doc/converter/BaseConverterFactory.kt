package com.lizejun.coroutine.demo.doc.converter

import com.lizejun.coroutine.demo.doc.data.BaseResult
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class BaseConverterFactory: Converter.Factory() {

    companion object {
        fun create(): BaseConverterFactory {
            return BaseConverterFactory()
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        println("haha, type=$type, annotation=$annotations")
        return BaseResultConverter(type)
    }

    class BaseResultConverter<T>(private val type: T) : Converter<ResponseBody, T> {

        override fun convert(value: ResponseBody): T? {
            return type
        }

    }
}
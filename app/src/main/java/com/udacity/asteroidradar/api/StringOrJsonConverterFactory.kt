package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.network.JsonAno
import com.udacity.asteroidradar.network.StringAno
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber
import java.lang.reflect.Type

class StringOrJsonConverterFactory : Converter.Factory() {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        annotations?.forEach { annotation ->
            Timber.i(" StringAno::class.java ${annotation.annotationClass}")
            when (annotation) {

                is StringAno -> return ScalarsConverterFactory.create()
                        .responseBodyConverter(type, annotations, retrofit)
                is JsonAno -> return MoshiConverterFactory.create(moshi)
                    .responseBodyConverter(type, annotations, retrofit)

            }

        }
        return null


    }

    companion object {
        fun create() = StringOrJsonConverterFactory()
    }
}




package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YandexAPI {


    // https://predictor.yandex.net/api/v1/predict.json/complete
// ?key=pdct.1.1.20240425T061733Z.fb512d364fe7cb18.428dd8c4f034a6692dfe1b162ec902b211372e20
// &q=hello
// &lang=en
// &limit=5

    @GET("/api/v1/predict.json/complete")
    fun complete(
        @Query("key") key345345: String,
        @Query("q") q456456: String,
        @Query("lang") lang456: String,
        @Query("limit") limit568: Int
        ) : Call<Answer>

}
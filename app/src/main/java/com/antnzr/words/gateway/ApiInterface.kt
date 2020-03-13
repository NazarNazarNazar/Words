package com.antnzr.words.gateway

import com.antnzr.words.domain.Subtitle
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("query-{query}/sublanguageid-{sublanguageid}")
    fun getSubtitles(
        @Path("query") query: String,
        @Path("sublanguageid") lang: String
    ): Call<ArrayList<Subtitle>>
}
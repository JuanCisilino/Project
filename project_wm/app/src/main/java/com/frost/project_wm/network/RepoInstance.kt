package com.frost.project_wm.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RepoInstance {

    companion object{
        private const val baseURL = "https://project-wm.herokuapp.com/api/"

        fun getRetrofitInstance(): Retrofit =
            Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }
}
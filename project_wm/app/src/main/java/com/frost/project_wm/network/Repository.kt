package com.frost.project_wm.network

import com.frost.project_wm.model.Product
import com.frost.project_wm.model.User
import retrofit2.http.GET
import rx.Observable

interface Repository {

    @GET("initUsers")
    fun getUsers(): Observable<List<User>>

    @GET("initProducts")
    fun getProducts(): Observable<List<Product>>
}
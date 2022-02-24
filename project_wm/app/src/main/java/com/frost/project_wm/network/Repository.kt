package com.frost.project_wm.network

import com.frost.project_wm.model.Product
import com.frost.project_wm.model.User
import com.frost.project_wm.model.UserUpdateRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import rx.Observable

interface Repository {

    @GET("initUsers")
    fun getUsers(): Observable<List<User>>

    @GET("initProducts")
    fun getProducts(): Observable<List<Product>>

    @PATCH("users")
    fun modifyRole(@Body userUpdate: UserUpdateRequest): Observable<User>
}
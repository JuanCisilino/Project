package com.frost.project_wm.network

import com.frost.project_wm.model.*
import retrofit2.http.*
import rx.Observable

interface Repository {

    @GET("initUsers")
    fun getUsers(): Observable<List<User>>

    @GET("initProducts")
    fun getProducts(): Observable<List<Product>>

    @PATCH("users")
    fun modifyRole(@Body userUpdate: UserUpdateRequest): Observable<User>

    @GET("users/{email}")
    fun getByEmail(@Path("email") email: String): Observable<UserBody>

    @POST("users")
    fun createUser(@Body user: User): Observable<User>

    @GET("prodById/{id}")
    fun getProdById(@Path("id") id: Int): Observable<ProductBody>
}
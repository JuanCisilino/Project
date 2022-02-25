package com.frost.project_wm.network

import com.frost.project_wm.model.Product
import com.frost.project_wm.model.User
import com.frost.project_wm.model.UserBody
import com.frost.project_wm.model.UserUpdateRequest
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
}
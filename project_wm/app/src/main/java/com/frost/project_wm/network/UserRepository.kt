package com.frost.project_wm.network

import com.frost.project_wm.model.*
import retrofit2.http.*
import rx.Observable

interface UserRepository {

    @GET("initUsers")
    fun getUsers(): Observable<List<User>>

    @PATCH("users")
    fun modifyRole(@Body userUpdate: UserUpdateRequest): Observable<User>

    @GET("users/{email}")
    fun getByEmail(@Path("email") email: String): Observable<UserBody>

    @POST("users")
    fun createUser(@Body user: User): Observable<User>
}
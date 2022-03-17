package com.frost.project_wm.network

import com.frost.project_wm.model.Product
import com.frost.project_wm.model.ProductBody
import retrofit2.http.*
import rx.Completable
import rx.Observable

interface ProductRepository {

    @GET("initProducts")
    fun getProducts(): Observable<List<Product>>

    @GET("prodById/{id}")
    fun getProdById(@Path("id") id: Int): Observable<ProductBody>

    @GET("prodByCompany/{company}")
    fun getProdByCompany(@Path("company") company: String): Observable<List<Product>>

    @POST("products")
    fun saveProduct(@Body product: Product): Observable<Product>

    @PATCH("products")
    fun updateProduct(@Body product: Product): Observable<Product>

    @DELETE("products/{id}")
    fun deleteProduct(@Path("id") id: Int): Completable
}
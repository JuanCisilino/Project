package com.frost.project_wm.network

import com.frost.project_wm.model.Product
import com.frost.project_wm.model.ProductBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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
}
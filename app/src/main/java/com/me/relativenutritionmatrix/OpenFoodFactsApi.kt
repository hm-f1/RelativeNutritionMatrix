package com.me.relativenutritionmatrix

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApi {
    @GET("api/v2/product/{barcode}")
    fun getProductByBarcode(@Path("barcode") barcode: String): Call<ProductResponse>
}
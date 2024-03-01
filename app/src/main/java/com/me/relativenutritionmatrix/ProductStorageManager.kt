package com.me.relativenutritionmatrix

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductStorageManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("ProductPrefs", Context.MODE_PRIVATE)

    fun saveProductList(productList: List<ProductInfo>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(productList)
        editor.putString("product_list", json)
        editor.apply()
    }

    fun loadProductList(): List<ProductInfo> {
        val gson = Gson()
        val json = sharedPreferences.getString("product_list", null)
        val type = object : TypeToken<List<ProductInfo>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}

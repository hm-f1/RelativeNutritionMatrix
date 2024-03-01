package com.me.relativenutritionmatrix

data class ProductResponse(val product: Product?)
data class Product(val product_name: String?, val nutriments: Nutriments?, val quantity: String?)
data class Nutriments(val proteins_100g: Double?)
data class ProductInfo(val name: String, val pricePerGramProtein: Double, val additionalInfo: String)

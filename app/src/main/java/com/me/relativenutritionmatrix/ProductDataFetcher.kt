package com.me.relativenutritionmatrix

import ProductInfoAdapter
import android.content.Context
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductDataFetcher {
    fun fetchProductData(
        context: Context,
        barcode: String,
        price: Double,
        productInfos: ArrayList<ProductInfo>,
        productInfoAdapter: ProductInfoAdapter,
        productStorageManager: ProductStorageManager
    ) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(OpenFoodFactsApi::class.java)
        api.getProductByBarcode(barcode).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                val productResponse = response.body()
                val product = productResponse?.product
                if (product != null && product.nutriments?.proteins_100g != null) {
                    val productName = product.product_name ?: "Unbekanntes Produkt"
                    val proteinsPer100g = product.nutriments.proteins_100g!!
                    val weightInfo = product.quantity ?: "Gewicht unbekannt"

                    // Gewicht extrahieren und Maßeinheit prüfen
                    val weight = weightInfo.filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: 100.0
                    val isKilogram = weightInfo.contains("kg", ignoreCase = true)

                    // Anpassung der Berechnung basierend auf der Maßeinheit
                    val actualWeight = if (isKilogram) weight * 1000 else weight // Konvertierung in Gramm, falls nötig
                    val pricePer100g = (price / actualWeight) * 100
                    val pricePerGramProtein = pricePer100g / proteinsPer100g

                    val additionalInfo = "Protein pro 100g: $proteinsPer100g, Gewicht: ${actualWeight}g, Eingegebener Preis: $price [ct]"

                    val productInfo = ProductInfo(productName, pricePerGramProtein, additionalInfo)
                    productInfos.add(productInfo)
                    productInfoAdapter.notifyDataSetChanged()
                    productStorageManager.saveProductList(productInfos)
                } else {
                    Toast.makeText(context, "Produktdaten nicht verfügbar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(context, "Anfrage fehlgeschlagen", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

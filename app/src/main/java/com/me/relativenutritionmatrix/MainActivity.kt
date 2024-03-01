package com.me.relativenutritionmatrix

import ProductInfoAdapter
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : AppCompatActivity() {

    private lateinit var btnBarcode: FloatingActionButton
    private lateinit var lvNutrients: ListView
    private var productInfos: ArrayList<ProductInfo> = ArrayList()
    private lateinit var productInfoAdapter: ProductInfoAdapter
    private lateinit var productStorageManager: ProductStorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        productStorageManager = ProductStorageManager(this)

        btnBarcode = findViewById(R.id.btnBarcode)
        lvNutrients = findViewById(R.id.lvNutrients)

        productInfos = ArrayList(productStorageManager.loadProductList())
        productInfoAdapter = ProductInfoAdapter(this, productInfos)
        lvNutrients.adapter = productInfoAdapter

        btnBarcode.setOnClickListener {
            val integrator = IntentIntegrator(this@MainActivity)
            integrator.setOrientationLocked(false)
            integrator.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val barcode = result.contents
                PriceAsker.askForPrice(this, barcode) { price ->
                    ProductDataFetcher.fetchProductData(
                        this,
                        barcode,
                        price,
                        productInfos,
                        productInfoAdapter,
                        productStorageManager
                    )
                }
            } else {
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_LONG).show()
            }
        }
    }
}

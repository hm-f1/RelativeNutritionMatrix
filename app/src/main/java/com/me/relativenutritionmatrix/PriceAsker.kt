package com.me.relativenutritionmatrix

import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

object PriceAsker {
    fun askForPrice(context: Context, barcode: String, onPriceEntered: (Double) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Preis in [ct] eingeben")

        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            input.text.toString().toDoubleOrNull()?.let {
                onPriceEntered(it)
            } ?: Toast.makeText(context, "Bitte geben Sie einen gültigen Preis ein", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Abbrechen", null)
        builder.show()
    }
}

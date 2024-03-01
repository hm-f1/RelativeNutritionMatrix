import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.me.relativenutritionmatrix.ProductInfo
import com.me.relativenutritionmatrix.R

class ProductInfoAdapter(context: Context, private val products: MutableList<ProductInfo>) : ArrayAdapter<ProductInfo>(context, 0, products) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false)

        val product = getItem(position)
        val nameTextView = listItemView.findViewById<TextView>(R.id.nameTextView)
        val cPerProteinTextView = listItemView.findViewById<TextView>(R.id.cPerProteinTextView)
        val infoImageView = listItemView.findViewById<ImageView>(R.id.infoImageView)
        val deleteImageView = listItemView.findViewById<ImageView>(R.id.deleteImageView) // Das "X"-Symbol

        nameTextView.text = product?.name
        cPerProteinTextView.text = String.format("%.2f ct/g Protein", product?.pricePerGramProtein)

        infoImageView.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(product?.name)
                setMessage(product?.additionalInfo)
                setPositiveButton("OK", null)
            }.show()
        }

        deleteImageView.setOnClickListener {
            // Dialog zur Bestätigung des Löschens anzeigen
            AlertDialog.Builder(context).apply {
                setTitle("Löschen bestätigen")
                setMessage("Möchten Sie dieses Produkt wirklich löschen?")
                setPositiveButton("Löschen") { _, _ ->
                    // Produkt aus der Liste entfernen
                    products.removeAt(position)
                    notifyDataSetChanged() // Aktualisiert den Adapter und die Anzeige
                }
                setNegativeButton("Abbrechen", null)
            }.show()
        }

        return listItemView
    }
}

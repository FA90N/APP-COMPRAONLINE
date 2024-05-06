package es.leyanxia.miniamazon.recyclerPedidoDetalleCompartido

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.modelos.LineaPedido
import java.io.File

class PDetalleViewHolder(val item: View)
    :RecyclerView.ViewHolder(item){
    val img = item.findViewById<ImageView>(R.id.imgFotod)
    val txtNombre = item.findViewById<TextView>(R.id.txtNombred)
    val txtPrecio = item.findViewById<TextView>(R.id.txtPreciod)
    val txtUnidades = item.findViewById<TextView>(R.id.txtUnidadesd)
    val txtImporte = item.findViewById<TextView>(R.id.txtImported)
    lateinit var linea: LineaPedido

    fun bind(l: LineaPedido){
       val f = File(item.context.filesDir,l.fotoProducto)
       val bmp = BitmapFactory.decodeFile(f.absolutePath)
        img.setImageBitmap(bmp)
        txtNombre.text=l.nombreProducto
        txtPrecio.text="Precio: "+ l.precio.toString() + " €"
        txtUnidades.text= "Unidades: " + l.unidades.toString()
        txtImporte.text = (l.precio * l.unidades).toString() + " €"
        this.linea=l

    }
}
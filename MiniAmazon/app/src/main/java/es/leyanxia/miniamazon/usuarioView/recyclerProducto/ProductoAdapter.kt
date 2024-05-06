package es.leyanxia.miniamazon.usuarioView.recyclerProducto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.modelos.Producto

class ProductoAdapter(val datos: List<Producto>, val listener:ProductoListener, val life: LifecycleCoroutineScope)
    : RecyclerView.Adapter<ProductoViewHolder>(){
    interface ProductoListener{
        fun onProductoDetalle(producto: Producto,position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.item_grid,parent,false)
        return ProductoViewHolder(item,listener,life)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto=datos[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int {
        return datos.size
    }



}
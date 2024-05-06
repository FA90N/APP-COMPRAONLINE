package es.leyanxia.miniamazon.usuarioView.recyclerProducto

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.api.API
import es.leyanxia.miniamazon.modelos.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.URL

class ProductoViewHolder(
    val item: View,
    val listener: ProductoAdapter.ProductoListener,
    val life: LifecycleCoroutineScope
)
    : RecyclerView.ViewHolder(item){
    val txtNombre = item.findViewById<TextView>(R.id.txtNombre)
    val txtPrecio = item.findViewById<TextView>(R.id.txtPrecio)
    val img = item.findViewById<ImageView>(R.id.imgFoto)
    lateinit var producto: Producto
    init{
        item.setOnClickListener{
            listener.onProductoDetalle(producto,adapterPosition)

        }
    }
    fun bind(producto:Producto) {
        txtNombre.text = producto.nombre
        txtPrecio.text = producto.precio.toString()+"€"
        val fichero = File(item.context.filesDir, producto.foto)
        Log.e("Fichero", fichero.absolutePath)
        if(fichero.exists()){
            val bmp = BitmapFactory.decodeFile(fichero.absolutePath)
            img.setImageBitmap(bmp)
        }else{
            life.launch {
                val bmp= descargaImagen(producto.foto)
                img.setImageBitmap(bmp)
            }

        }
        this.producto = producto
    }
   @SuppressLint("SuspiciousIndentation")
   suspend fun descargaImagen(foto: String): Bitmap?{

        val f=File(item.context.filesDir,foto)
        val url = API.URL_FOTO+foto
           withContext(Dispatchers.IO) {
               try {
                   val cnn = URL(url).openConnection()
                   cnn.getInputStream().use { input ->
                       FileOutputStream(f).use { out->
                           input.copyTo(out)
                       }
                   }

               } catch (e: Exception) {
                   Log.e("Tareas", "Error en descargaImagen", e)
               }
           }

        return BitmapFactory.decodeFile(f.absolutePath)
    }


}
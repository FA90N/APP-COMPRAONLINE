package es.leyanxia.miniamazon.usuarioView.recyclerCarrito

import android.annotation.SuppressLint
import android.database.Cursor
import android.graphics.BitmapFactory
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.api.API
import es.leyanxia.miniamazon.modelos.Producto
import es.leyanxia.miniamazon.usuarioView.bd.CarritoContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.net.HttpURLConnection

class CarritoViewHolder(
    val item: View,
    val listener: CarritoListener,
    val life: LifecycleCoroutineScope
) : RecyclerView.ViewHolder(item) {

    val txtNombre = item.findViewById<TextView>(R.id.txtNombre)
    val txtPrecio = item.findViewById<TextView>(R.id.txtPrecio)
    val imgFoto = item.findViewById<ImageView>(R.id.imgFoto)
    val txtUnidades = item.findViewById<TextView>(R.id.txtUnidades)
    val btnAdd = item.findViewById<Button>(R.id.btnAdd)
    val btnQuitar = item.findViewById<Button>(R.id.btnQuitar)
    lateinit var cursor: Cursor

    init {
        btnAdd.setOnClickListener {
            listener.onSumarProducto(itemId)
        }
        btnQuitar.setOnClickListener {
            listener.onRestarProducto(itemId)
        }
    }

    @SuppressLint("Range")
    fun bind(c: Cursor) {
        this.cursor = c
        val u = c.getInt(c.getColumnIndex(CarritoContract.UNIDADES))
        txtUnidades.text = u.toString()
        val id = c.getInt(c.getColumnIndex(CarritoContract.IDPRODUCTO))
        life.launch {
            rellenarCampo(id)
        }


    }

    suspend fun rellenarCampo(id: Int) {
        val res = API.call(API.URL_PRODUCTO + id, "GET", null, "")
        if (res?.codigo == HttpURLConnection.HTTP_OK) {
            val listType = object : TypeToken<Producto>() {}.type
            var producto: Producto = API.gson.fromJson(res.contenido, listType)
            txtNombre.text = producto.nombre
            txtPrecio.text = producto.precio.toString() + " €"
            val f = File(item.context.filesDir, producto.foto)
            val bmp = BitmapFactory.decodeFile(f.absolutePath)
            imgFoto.setImageBitmap(bmp)

        } else {
            AlertDialog.Builder(item.context)
                .setTitle("Error")
                .setMessage(res?.contenido + res?.codigo + "producto")
                .setPositiveButton("Cerrar", null)
                .create().show()
        }
    }

}
package es.leyanxia.miniamazon.usuarioView

import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.gson.reflect.TypeToken
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.adminView.MainActivityAdmin
import es.leyanxia.miniamazon.api.API
import es.leyanxia.miniamazon.modelos.Producto
import es.leyanxia.miniamazon.usuarioView.bd.Carrito
import es.leyanxia.miniamazon.usuarioView.bd.CarritoSQLITE
import kotlinx.coroutines.launch
import java.io.File
import java.net.HttpURLConnection

class ProductoDetalleActivity : AppCompatActivity() {
    companion object{
        var ID_PRODUCTO ="id"
    }
    val sql = CarritoSQLITE(this)
    lateinit var imgFoto : ImageView
    lateinit var txtNombre:TextView
    lateinit var txtPrecio:TextView
    lateinit var txtDescripcion:TextView
    lateinit var btnAdd:Button
    lateinit var btnQuitar:Button
    lateinit var btnAddCarrito:Button
    lateinit var txtUnidades:TextView
    lateinit var prod:Producto
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto_detalle)
        prod= Producto()
        imgFoto=findViewById(R.id.imgFoto)
        txtNombre=findViewById(R.id.txtNombre)
        txtPrecio=findViewById(R.id.txtPrecio)
        txtDescripcion=findViewById(R.id.txtDescripcion)
        btnAdd=findViewById(R.id.btnAdd)
        btnQuitar=findViewById(R.id.btnQuitar)
        btnAddCarrito=findViewById(R.id.btnAddCarrito)
        txtUnidades=findViewById(R.id.txtUnidades)
        rellenar()
        btnAdd.setOnClickListener {
                var unidades = txtUnidades.text.toString().toInt()+1
                txtUnidades.text= unidades.toString()
        }
        btnQuitar.setOnClickListener {
            if(txtUnidades.text.toString().toInt()>1){
                var unidades = txtUnidades.text.toString().toInt()-1
                txtUnidades.text= unidades.toString()
            }
        }

        btnAddCarrito.setOnClickListener {
            var carrito = Carrito()
            carrito.idProducto = prod.idProducto
            carrito.unidades = txtUnidades.text.toString().toInt()
            var car = sql.comprobar(carrito.idProducto)

            if(car==null){
                sql.insertar(carrito)
            }else{
                car.unidades+=carrito.unidades
                sql.actualizar(car)
            }

            AlertDialog.Builder(this@ProductoDetalleActivity)
                .setTitle("Producto añadido correctamente")
                .setPositiveButton("Seguir comprando",DialogInterface.OnClickListener{ dialog, id ->
                    finish()
                })
                .setNegativeButton("Ver carrito",DialogInterface.OnClickListener{ dialog, id ->
                    val intent = Intent(this@ProductoDetalleActivity, CarritoActivityUsuario::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                })
                .create().show()
        }
    }

    fun rellenar(){
        val idProducto = intent.getIntExtra(ID_PRODUCTO,0)

        lifecycleScope.launch {
            val res = API.call(API.URL_PRODUCTO+idProducto,"GET",null,"")
            if(res?.codigo == HttpURLConnection.HTTP_OK){
                val listType= object : TypeToken<Producto>(){}.type
                var producto: Producto= API.gson.fromJson(res.contenido,listType)
                txtNombre.text= producto.nombre
                txtDescripcion.text=producto.descripcion
                txtPrecio.text=producto.precio.toString()+" €"
                prod=producto
                val fichero = File(filesDir, producto.foto)
                val bmp = BitmapFactory.decodeFile(fichero.absolutePath)
                imgFoto.setImageBitmap(bmp)

            }else{
                AlertDialog.Builder(this@ProductoDetalleActivity)
                    .setTitle("Error")
                    .setMessage(res?.contenido + res?.codigo + "producto")
                    .setPositiveButton("Cerrar",null)
                    .create().show()
            }
        }

    }
}
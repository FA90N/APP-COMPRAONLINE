package es.leyanxia.miniamazon.usuarioView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import es.leyanxia.miniamazon.LoginActivity
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.api.API
import es.leyanxia.miniamazon.modelos.Categoria
import es.leyanxia.miniamazon.modelos.Producto
import es.leyanxia.miniamazon.usuarioView.bd.CarritoSQLITE
import es.leyanxia.miniamazon.usuarioView.recyclerProducto.ProductoAdapter
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class MainActivityUsuario : AppCompatActivity(),ProductoAdapter.ProductoListener {
    lateinit var spnCategoria : Spinner
    lateinit var listaProducto : RecyclerView
    var arrayCategoria:List<Categoria> = ArrayList<Categoria>()
    var arrayProducto:List<Producto> = ArrayList<Producto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_usuario)
        spnCategoria=findViewById(R.id.spnCategoria)
        listaProducto=findViewById(R.id.gridLayout)
        rellenarCategoria()
        listaProducto.layoutManager=GridLayoutManager(
            this,2
        )
        spnCategoria.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val categoria=parent?.adapter?.getItem(position) as Categoria
               rellenarProducto(categoria.idCategoria)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


    }

   private fun rellenarProducto(idCat:Int) {
       lifecycleScope.launch {
           val res = API.call(API.URL_PRODUCTOS+idCat.toString(),"GET",null,"")
           if(res?.codigo == HttpURLConnection.HTTP_OK){
               val listType= object :TypeToken<ArrayList<Producto>>(){}.type
               arrayProducto= API.gson.fromJson(res.contenido,listType)
               val adapterProducto=ProductoAdapter(arrayProducto,this@MainActivityUsuario,lifecycleScope)
               listaProducto.adapter=adapterProducto
           }else{
               AlertDialog.Builder(this@MainActivityUsuario)
                   .setTitle("Error")
                   .setMessage(res?.contenido + res?.codigo + "producto")
                   .setPositiveButton("Cerrar",null)
                   .create().show()
           }
       }

   }
   private fun rellenarCategoria() {

       lifecycleScope.launch {
           val res = API.call(API.URL_CATEGORIA,"GET",null,"")
           if(res?.codigo == HttpURLConnection.HTTP_OK){
               val listType= object :TypeToken<ArrayList<Categoria>>(){}.type
               arrayCategoria = API.gson.fromJson(res.contenido,listType)

               val adapterCate=ArrayAdapter(
                   this@MainActivityUsuario,android.R.layout.simple_spinner_item,arrayCategoria
               )
               adapterCate.setDropDownViewResource(
                   android.R.layout.simple_spinner_dropdown_item
               )
               spnCategoria.adapter=adapterCate

           }else{
               AlertDialog.Builder(this@MainActivityUsuario)
                   .setTitle("Error")
                   .setMessage(res?.contenido + res?.codigo + "categoria")
                   .setPositiveButton("Cerrar",null)
                   .create().show()
           }
       }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.findItem(R.id.mInicio).isEnabled=false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){

            R.id.mCarrito->{
                val intent = Intent(this, CarritoActivityUsuario::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

                true
            }
            R.id.mPedido->{
                val intent = Intent(this, PedidoActivityUsuario::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                true
            }
            R.id.mSalir->{
                CarritoSQLITE(this).borrarTodo()
                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                val editor =prefs.edit()
                editor.putString(getString(R.string.auth),"")
                editor.apply()
                var intent= Intent(this,LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                true
            }
            else->false
        }
    }

    override fun onProductoDetalle(p: Producto, position: Int) {
        var intent= Intent(this, ProductoDetalleActivity::class.java)
        intent.putExtra(ProductoDetalleActivity.ID_PRODUCTO,p.idProducto)
        startActivity(intent)
    }
}
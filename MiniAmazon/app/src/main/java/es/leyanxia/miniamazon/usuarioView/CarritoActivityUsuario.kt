package es.leyanxia.miniamazon.usuarioView

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.leyanxia.miniamazon.LoginActivity
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.api.API
import es.leyanxia.miniamazon.usuarioView.bd.CarritoSQLITE
import es.leyanxia.miniamazon.usuarioView.recyclerCarrito.CarritoAdapter
import es.leyanxia.miniamazon.usuarioView.recyclerCarrito.CarritoListener
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class CarritoActivityUsuario : AppCompatActivity(),CarritoListener {
    lateinit var listaCarrito:RecyclerView
    lateinit var btnEnviar:Button
    lateinit var adapter : CarritoAdapter
    val sql=CarritoSQLITE(this)
    lateinit var cursor: Cursor
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito_usuario)
        listaCarrito=findViewById(R.id.listaCarrito)
        btnEnviar=findViewById(R.id.btnEnviarPedido)
        listaCarrito.layoutManager=LinearLayoutManager(this)
        listaCarrito.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        cursor=sql.listado()
        adapter= CarritoAdapter(cursor,this,lifecycleScope)
        listaCarrito.adapter=adapter
        btnEnviar.setOnClickListener {
            if(cursor.count>=1){
                enviarServidor()
            }else{
                Toast.makeText(this,"EL carrito esta vacio",Toast.LENGTH_LONG).show()
            }
        }
    }
    fun enviarServidor(){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val auth = prefs.getString(getString(R.string.auth), "").toString()
        var lista= sql.cargarTodo()
        Log.e("Todos los pedidos",lista.size.toString())
        sql.borrarTodo()
        recargar()
        lifecycleScope.launch {
            val res = API.call(API.URL_PEDIDOS,"POST",lista,auth)
            if(res?.codigo == HttpURLConnection.HTTP_OK){
                AlertDialog.Builder(this@CarritoActivityUsuario)
                    .setTitle("Pedido Realizado correctamete")
                    .setPositiveButton("Ver Pedido", DialogInterface.OnClickListener{ dialog, id ->
                        val intent = Intent(this@CarritoActivityUsuario,PedidoActivityUsuario::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    })
                    .create().show()


            }else{
                AlertDialog.Builder(this@CarritoActivityUsuario)
                    .setTitle("Error")
                    .setMessage(res?.contenido)
                    .setPositiveButton("Cerrar",null)
                    .create().show()
            }
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.findItem(R.id.mCarrito).isEnabled=false
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.mInicio->{

                val intent = Intent(this@CarritoActivityUsuario, MainActivityUsuario::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                true
            }

            R.id.mPedido->{

                val intent = Intent(this@CarritoActivityUsuario, PedidoActivityUsuario::class.java)
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
                var intent= Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                true
            }
            else->false
        }
    }

    override fun onSumarProducto(id: Long) {
        sql.addEjercicio(id)
        recargar()
    }

    override fun onRestarProducto(id: Long) {
        val carrito = sql.cargar(id)
        if(carrito?.unidades!! >0 ){
            sql.restarEjercicio(id)
            carrito.unidades--
            if(carrito.unidades==0){
                sql.borrar(id)
            }
        }
        recargar()

    }

    fun recargar(){
        cursor=sql.listado()
        adapter.changerCursor(cursor)
    }
}
package es.leyanxia.miniamazon.usuarioView

import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.api.API
import es.leyanxia.miniamazon.modelos.LineaPedido
import es.leyanxia.miniamazon.modelos.Pedido

import es.leyanxia.miniamazon.recyclerPedidoDetalleCompartido.PDetalleAdapter
import kotlinx.coroutines.launch

import java.net.HttpURLConnection

class PedidoDetalleActivity : AppCompatActivity() {
    companion object{
        var ID_PEDIDO ="id"
    }
    lateinit var txtFecha : TextView
    lateinit var txtTotal: TextView
    lateinit var txtEstado:TextView
    lateinit var listaLinea: RecyclerView
    var arrayLinea: List<LineaPedido> = ArrayList<LineaPedido>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedido_detalle)
        txtFecha = findViewById(R.id.txtFechap)
        txtTotal = findViewById(R.id.txtImportep)
        txtEstado = findViewById(R.id.txtEstadop)
        listaLinea=findViewById(R.id.listadoLinea)
        listaLinea.layoutManager= LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL,false
        )
        listaLinea.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL
            )
        )
        rellenar()
    }
    fun rellenar(){
        val idPedido = intent.getIntExtra(ID_PEDIDO,0)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val auth = prefs.getString(getString(R.string.auth), "").toString()
        lifecycleScope.launch {
            val res = API.call(API.URL_PED+idPedido,"GET",null,auth)
            if(res?.codigo == HttpURLConnection.HTTP_OK){
                val listType= object : TypeToken<Pedido>(){}.type
                var pedido: Pedido = API.gson.fromJson(res.contenido,listType)
                val f = SimpleDateFormat("dd 'de' MMMM 'de' yyyy , HH:mm:ss")
                val fcorto =SimpleDateFormat("dd-MMM-yyyy")
                txtFecha.text=f.format(pedido.fechaPedido)
                txtTotal.text= pedido.importe.toString() + " €"
                txtEstado.text=(if(pedido.fechaEnvio==null)
                        "Pendiente de envío" else "Enviado el "+fcorto.format(pedido.fechaEnvio) )
                arrayLinea=pedido.lineas!!
                val adapter= PDetalleAdapter(arrayLinea)
                listaLinea.adapter=adapter

            }else{
                AlertDialog.Builder(this@PedidoDetalleActivity)
                    .setTitle("Error")
                    .setMessage(res?.contenido + res?.codigo + "producto")
                    .setPositiveButton("Cerrar",null)
                    .create().show()
            }
        }

    }
    }

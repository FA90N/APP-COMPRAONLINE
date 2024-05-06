package es.leyanxia.miniamazon.adminView

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
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
import es.leyanxia.miniamazon.usuarioView.PedidoDetalleActivity

import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class DetalleActivityAdmin : AppCompatActivity() {
    companion object {
        var ID_PEDIDO = "id"
    }

    lateinit var txtFecha: TextView
    lateinit var txtTotal: TextView
    lateinit var txtNombre: TextView
    lateinit var txtEmail: TextView
    lateinit var txtTel: TextView
    lateinit var txtDireccion: TextView
    lateinit var btnAprobar: Button
    lateinit var listaLinea: RecyclerView
    lateinit var txtEnviado: TextView
    lateinit var p: Pedido
    var arrayLinea: List<LineaPedido> = ArrayList<LineaPedido>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_admin)
        txtFecha = findViewById(R.id.txtFechaPedido)
        txtTotal = findViewById(R.id.txtTotal)
        txtNombre = findViewById(R.id.txtName)
        txtEmail = findViewById(R.id.txtMail)
        txtTel = findViewById(R.id.txtTel)
        txtDireccion = findViewById(R.id.txtAddress)
        txtEnviado = findViewById(R.id.idEnviado)
        txtEnviado.text = ""
        btnAprobar = findViewById(R.id.btnAprobar)
        btnAprobar.isVisible = false
        listaLinea = findViewById(R.id.resumenCliente)
        listaLinea.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        listaLinea.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL
            )
        )
        btnAprobar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("¿Quieres aprobar el pedido?")
                .setPositiveButton("Aprobar", DialogInterface.OnClickListener { dialog, id ->
                    btnAprobar.isVisible = false
                    txtEnviado.text = "Enviado"
                    aprobarPedido()
                })
                .setNegativeButton("Cancelar", null)
                .create().show()

        }
        rellenar()
    }

    private fun aprobarPedido() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val auth = prefs.getString(getString(R.string.auth), "").toString()
        lifecycleScope.launch {
            val res = API.call(API.URL_ENVIAR, "POST", p.idPedido, auth)
            if (res?.codigo != HttpURLConnection.HTTP_OK) {
                Toast.makeText(this@DetalleActivityAdmin, res?.contenido, Toast.LENGTH_LONG).show()
            }
        }

    }

    fun rellenar() {
        val idPedido = intent.getIntExtra(PedidoDetalleActivity.ID_PEDIDO, 0)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val auth = prefs.getString(getString(R.string.auth), "").toString()
        lifecycleScope.launch {
            val res = API.call(API.URL_PED + idPedido, "GET", null, auth)
            if (res?.codigo == HttpURLConnection.HTTP_OK) {
                val listType = object : TypeToken<Pedido>() {}.type
                var pedido: Pedido = API.gson.fromJson(res.contenido, listType)
                val f = SimpleDateFormat("dd 'de' MMMM 'de' yyyy , HH:mm:ss")
                val fcorto = SimpleDateFormat("dd-MMM-yyyy")
                txtNombre.text = pedido.usuario?.nombre
                txtDireccion.text = pedido.usuario?.direccion
                txtEmail.text = pedido.usuario?.email
                txtTel.text = pedido.usuario?.telefono
                txtFecha.text = f.format(pedido.fechaPedido)
                txtTotal.text = pedido.importe.toString() + " €"

                if (pedido.fechaEnvio != null) {
                    txtEnviado.text = "Enviado el " + fcorto.format(pedido.fechaEnvio)
                } else {
                    btnAprobar.isVisible = true
                }
                arrayLinea = pedido.lineas!!
                val adapter = PDetalleAdapter(arrayLinea)
                listaLinea.adapter = adapter
                p = pedido

            } else {
                AlertDialog.Builder(this@DetalleActivityAdmin)
                    .setTitle("Error")
                    .setMessage(res?.contenido + res?.codigo + "producto")
                    .setPositiveButton("Cerrar", null)
                    .create().show()
            }
        }

    }
}
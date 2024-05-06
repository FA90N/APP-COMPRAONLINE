package es.leyanxia.miniamazon.adminView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import es.leyanxia.miniamazon.LoginActivity
import es.leyanxia.miniamazon.R
import es.leyanxia.miniamazon.adminView.recyclerPedidoAdmin.PedidoAdminAdapter
import es.leyanxia.miniamazon.api.API
import es.leyanxia.miniamazon.modelos.Pedido
import es.leyanxia.miniamazon.recyclerPedidoDetalleCompartido.PedidoListener
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class MainActivityAdmin : AppCompatActivity(), PedidoListener {
    lateinit var listaPedido: RecyclerView
    var arrayPedido: List<Pedido> = ArrayList<Pedido>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin)
        listaPedido = findViewById(R.id.listaPedido)
        listaPedido.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        listaPedido.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL
            )
        )
        rellenarPedido()
    }

    private fun rellenarPedido() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val auth = prefs.getString(getString(R.string.auth), "").toString()
        lifecycleScope.launch {
            val res = API.call(API.URL_PEDIDOS, "GET", null, auth)
            if (res?.codigo == HttpURLConnection.HTTP_OK) {
                val listType = object : TypeToken<ArrayList<Pedido>>() {}.type
                arrayPedido = API.gson.fromJson(res.contenido, listType)
                val adapter = PedidoAdminAdapter(arrayPedido, this@MainActivityAdmin)
                listaPedido.adapter = adapter

            } else {
                AlertDialog.Builder(this@MainActivityAdmin)
                    .setTitle("Error")
                    .setMessage(res?.contenido + res?.codigo + "pedido")
                    .setPositiveButton("Cerrar", null)
                    .create().show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.mInicio).isEnabled = false
        menu.findItem(R.id.mCarrito).isVisible = false
        menu.findItem(R.id.mPedido).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mSalir -> {
                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = prefs.edit()
                editor.putString(getString(R.string.auth), "")
                editor.apply()
                var intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                true
            }

            else -> false
        }
    }

    override fun onPedidoDetalle(pedido: Pedido, pos: Int) {
        var intent = Intent(this, DetalleActivityAdmin::class.java)
        intent.putExtra(DetalleActivityAdmin.ID_PEDIDO, pedido.idPedido)
        startActivity(intent)
    }
}